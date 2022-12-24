package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.cache.repository.insight.CInsightAggregationRepository;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.enums.InsightTitle;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


@Service
@Slf4j
public class InsightTitleStatService extends AbstractTitleStatService<Long, InsightTitle> {
    private final CInsightAggregationRepository insightAggregationRepository;
    private final TitleRepository titleRepository;

    public InsightTitleStatService(MQPublishService mqPublishService,
                                   CInsightAggregationRepository insightAggregationRepository,
                                   TitleAchievementRepository titleAchievementRepository,
                                   TitleRepository titleRepository,
                                   UserDomainService userDomainService) {

        super(mqPublishService, titleAchievementRepository, userDomainService);
        this.insightAggregationRepository = insightAggregationRepository;
        this.titleRepository = titleRepository;
    }

    @Override
    public Long aggregate(KeeweTitleHeader header) {
        insightAggregationRepository.incrementInsightCount(header.getUserId());
        return insightAggregationRepository.get(header.getUserId());
    }

    @Override
    protected Optional<InsightTitle> getAcquiredTitleOps(Long target) {
        return Arrays.stream(InsightTitle.values())
                .filter(it -> it.standard == target)
                .map(Optional::of)
                .findFirst()
                .orElseGet(() ->Optional.ofNullable(null));
    }

    @Override
    protected Title saveAndGetTitle(InsightTitle param) {
        return titleRepository.findById(param.id).orElseThrow();
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.INSIGHT;
    }

}
