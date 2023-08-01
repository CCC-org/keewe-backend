package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.cache.domain.insight.CReactionCountForTitle;
import ccc.keewedomain.cache.repository.insight.CReactionCountTitleRepository;
import ccc.keewedomain.persistence.domain.title.enums.ReactionTitle;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class ReactionCountTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final CReactionCountTitleRepository cReactionCountTitleRepository;

    public ReactionCountTitleAcquireProcessor(
            MQPublishService mqPublishService,
            CReactionCountTitleRepository cReactionCountTitleRepository,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository,
            NotificationCommandDomainService notificationCommandDomainService
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository, notificationCommandDomainService);
        this.cReactionCountTitleRepository = cReactionCountTitleRepository;
    }

    // @todo : 분산락 적용 필요 (scale-out 시)
    @Override
    protected synchronized List<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        Long userId = Long.valueOf(header.getUserId());
        long cacheValue = cReactionCountTitleRepository.findByIdWithMissHandle(userId).getCount();
        CReactionCountForTitle current = CReactionCountForTitle.of(userId, cacheValue + 1);
        cReactionCountTitleRepository.save(current);
        if (current.getCount() == 1L)
            return List.of(ReactionTitle.리엑션_최초.getId());
        if (current.getCount() == 50L)
            return List.of(ReactionTitle.리엑션_50회.getId());
        return List.of();
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.REACTION;
    }
}
