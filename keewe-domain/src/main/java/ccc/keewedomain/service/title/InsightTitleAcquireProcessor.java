package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.cache.repository.insight.CInsightAggregationRepository;
import ccc.keewedomain.persistence.domain.title.enums.InsightTitle;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InsightTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final CInsightAggregationRepository insightAggregationRepository;

    public InsightTitleAcquireProcessor(
        MQPublishService mqPublishService,
        CInsightAggregationRepository insightAggregationRepository,
        TitleAchievementRepository titleAchievementRepository,
        UserDomainService userDomainService,
        UserRepository userRepository,
        TitleRepository titleRepository
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository);
        this.insightAggregationRepository = insightAggregationRepository;
    }

    @Override
    public List<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        insightAggregationRepository.incrementInsightCount(header.getUserId());
        Long insightCount = insightAggregationRepository.get(header.getUserId());
        return Arrays.stream(InsightTitle.values())
                .sorted(Comparator.comparingLong(InsightTitle::getStandard).reversed())
                .filter(it -> it.getStandard() <= insightCount)
                .map(InsightTitle::getId)
                .collect(Collectors.toList());
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.INSIGHT;
    }
}
