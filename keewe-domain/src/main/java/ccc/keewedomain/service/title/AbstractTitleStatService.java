package ccc.keewedomain.service.title;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.dto.TitleEvent;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.MQPublishService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


public abstract class AbstractTitleStatService<T, S> {
    private final MQPublishService mqPublishService;
    private final TitleAchievementRepository titleAchievementRepository;
    private final UserDomainService userDomainService;

    protected AbstractTitleStatService(MQPublishService mqPublishService,
                                       TitleAchievementRepository titleAchievementRepository,
                                       UserDomainService userDomainService) {
        this.mqPublishService = mqPublishService;
        this.titleAchievementRepository = titleAchievementRepository;
        this.userDomainService = userDomainService;
    }

    @Transactional
    public void aggregateStat(KeeweTitleHeader header) {
        T result = aggregate(header);

        getTitleOpts(result).ifPresent(s -> {
                    Title title = saveAndGetTitle(s);

                    User user = userDomainService.getUserByIdOrElseThrow(Long.valueOf(header.getUserId()));
                    titleAchievementRepository.save(TitleAchievement.of(user, title));

                    TitleEvent event = TitleEvent.of(title.getCategory(), title.getName(),title.getIntroduction(), LocalDateTime.now());
                    mqPublishService.publish(KeeweConsts.TITLE_ACQUIREMENT_EXCHANGE, event);
                });
    }

    protected abstract T aggregate(KeeweTitleHeader header);

    protected abstract Optional<S> getTitleOpts(T target);

    protected abstract Title saveAndGetTitle(S param);

    public abstract TitleCategory getProcessableCategory();
}
