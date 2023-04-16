package ccc.keewedomain.service.title;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.dto.TitleEvent;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewecore.utils.ObjectMapperUtils;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


public abstract class AbstractTitleAcquireProcessor {
    private final MQPublishService mqPublishService;
    private final TitleAchievementRepository titleAchievementRepository;
    private final UserDomainService userDomainService;
    private final TitleRepository titleRepository;

    protected AbstractTitleAcquireProcessor(
        MQPublishService mqPublishService,
        TitleAchievementRepository titleAchievementRepository,
        UserDomainService userDomainService,
        TitleRepository titleRepository
    ) {
        this.mqPublishService = mqPublishService;
        this.titleAchievementRepository = titleAchievementRepository;
        this.userDomainService = userDomainService;
        this.titleRepository = titleRepository;
    }

    @Transactional
    public void aggregateStat(KeeweTitleHeader header) {
        // 타이틀 획득 조건 충족 체크
        judgeTitleAcquire(header).ifPresent(titleId -> {
            // 타이틀 조회
            Title title = titleRepository.findById(titleId).orElseThrow();

            // 타이틀 획득 정보 저장
            User user = userDomainService.getUserByIdOrElseThrow(Long.valueOf(header.getUserId()));
            titleAchievementRepository.save(TitleAchievement.of(user, title));

            // 타이틀 획득 이벤트 발행
            TitleEvent event = TitleEvent.of(title.getCategory(), title.getName(),title.getIntroduction(), LocalDateTime.now());
            Message message = MessageBuilder.withBody(ObjectMapperUtils.writeValueAsBytes(event)).build();
            mqPublishService.publish(KeeweConsts.TITLE_ACQUIREMENT_EXCHANGE, KeeweConsts.DEFAULT_ROUTING_KEY, message, header::toMessageWithHeader);
        });
    }

    public abstract TitleCategory getProcessableCategory();

    // 통계 정보 집계, 타이틀 획득 기준 충족 시 타이틀 ID 반환
    protected abstract Optional<Long> judgeTitleAcquire(KeeweTitleHeader header);
}
