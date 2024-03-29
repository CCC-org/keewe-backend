package ccc.keewedomain.service.title;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.dto.TitleEvent;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewecore.utils.ObjectMapperUtils;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public abstract class AbstractTitleAcquireProcessor {
    private final MQPublishService mqPublishService;
    private final TitleAchievementRepository titleAchievementRepository;
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final TitleRepository titleRepository;
    private final NotificationCommandDomainService notificationCommandDomainService;

    protected AbstractTitleAcquireProcessor(
        MQPublishService mqPublishService,
        TitleAchievementRepository titleAchievementRepository,
        UserDomainService userDomainService,
        UserRepository userRepository,
        TitleRepository titleRepository,
        NotificationCommandDomainService notificationCommandDomainService
    ) {
        this.mqPublishService = mqPublishService;
        this.titleAchievementRepository = titleAchievementRepository;
        this.userDomainService = userDomainService;
        this.userRepository = userRepository;
        this.titleRepository = titleRepository;
        this.notificationCommandDomainService = notificationCommandDomainService;
    }

    @Transactional
    public void aggregateStat(KeeweTitleHeader header) {
        // 타이틀 획득 조건 충족 체크
        User user = userDomainService.getUserByIdOrElseThrow(Long.valueOf(header.getUserId()));
        this.judgeTitleAcquire(header).forEach(titleId -> {
            // 타이틀 조회
            Title title = titleRepository.findById(titleId).orElseThrow();

            // 이미 획득했으면 drop.
            Long titleArchivementCount = titleAchievementRepository.countByTitleAndUser(title, user);
            if (titleArchivementCount >= 1) {
                return;
            }

            // 타이틀 획득 정보 저장
            titleAchievementRepository.save(TitleAchievement.of(user, title));
            log.info("[TitleAcquireProcessor] 타이틀 획득 - titleId({}), userId({})", titleId, header.getUserId());
            Long titleAchievementCount = titleAchievementRepository.countByUser(user);

            // 알림 추가
            NotificationContents contents = NotificationContents.findByTitleId(title.getId());
            Notification notification = Notification.of(user, contents, "");
            notificationCommandDomainService.save(notification);

            // 최초 타이틀은 대표 타이틀로 설정
            if (titleAchievementCount <= 1) {
                user.updateRepTitle(title);
                userRepository.save(user);
            }

            // 타이틀 획득 이벤트 발행
            TitleEvent event = TitleEvent.of(title.getCategory(), title.getName(),title.getIntroduction(), LocalDateTime.now().toString());
            Message message = MessageBuilder.withBody(ObjectMapperUtils.writeValueAsBytes(event)).build();
            mqPublishService.publish(KeeweConsts.TITLE_ACQUIREMENT_EXCHANGE, KeeweConsts.DEFAULT_ROUTING_KEY, message, header::toMessageWithHeader);
        });
    }

    public abstract TitleCategory getProcessableCategory();

    // 통계 정보 집계, 타이틀 획득 기준 충족 시 타이틀 ID 반환
    protected abstract List<Long> judgeTitleAcquire(KeeweTitleHeader header);
}
