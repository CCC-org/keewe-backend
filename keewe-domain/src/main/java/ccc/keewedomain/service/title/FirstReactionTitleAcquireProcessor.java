package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.cache.domain.insight.CFirstReaction;
import ccc.keewedomain.cache.repository.insight.CFirstReactionAggregationRepository;
import ccc.keewedomain.persistence.domain.title.enums.ReactionTitle;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class FirstReactionTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final CFirstReactionAggregationRepository cFirstReactionAggregationRepository;

    public FirstReactionTitleAcquireProcessor(
            MQPublishService mqPublishService,
            CFirstReactionAggregationRepository cFirstReactionAggregationRepository,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository);
        this.cFirstReactionAggregationRepository = cFirstReactionAggregationRepository;
    }

    @Override
    protected Optional<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        Long userId = Long.valueOf(header.getUserId());
        boolean acquire = !cFirstReactionAggregationRepository.existsById(userId);
        if (!acquire)
            return Optional.empty();

        cFirstReactionAggregationRepository.save(CFirstReaction.of(userId));
        return Optional.of(ReactionTitle.리엑션_최초.getId());
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.REACTION;
    }
}
