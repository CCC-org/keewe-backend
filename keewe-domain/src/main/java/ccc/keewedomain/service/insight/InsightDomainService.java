package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.InsightViewIncrementDto;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightDomainService {

    private final InsightRepository insightRepository;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final MQPublishService mqPublishService;
    private final UserDomainService userDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final DrawerDomainService drawerDomainService;

    private final CInsightViewRepository cInsightViewRepository;

    //TODO 참가한 챌린지에 기록하기
    public Insight create(InsightCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());
        Drawer drawer = drawerDomainService.findById(dto.getDrawerId()).orElse(null);
        ChallengeParticipation participation = challengeDomainService.findCurrentChallengeParticipation(dto.getWriterId())
                .orElse(null);

        Insight insight = Insight.of(writer, participation, drawer, dto.getContents(), Link.of(dto.getLink()));
        insightRepository.save(insight);
        createReactionAggregations(insight);
        return insight;
    }

    private void createReactionAggregations(Insight insight) {
        Arrays.stream(ReactionType.values()).forEach((reactionType) -> {
            reactionAggregationRepository.save(ReactionAggregation.of(insight, reactionType, 0L));
        });
    }

    public Long incrementViewCount(InsightViewIncrementDto dto) {
        // 캐시 생성부터. 생성과 조회수 + 1이 역전될수있음
        Long viewCount = getViewCount(dto.getInsightId());

        log.info("[IDS::incrementViewCount] Cache Curr view {}", viewCount);
        mqPublishService.publish(KeeweConsts.INSIGHT_VIEW_EXCHANGE, String.valueOf(dto.getInsightId()));

        return viewCount;
    }

    @Transactional
    public Long incrementViewCount(Long insightId) {
        Insight insight = insightRepository.findByIdOrElseThrow(insightId);
        log.info("[IDS::incrementViewCount] DB Curr view {}, Next view {}", insight.getView(), insight.getView() + 1);
        return incrementViewCount(insight);
    }

    private Long incrementViewCount(Insight insight) {
        Long viewCount = insight.incrementView();

        CInsightView cInsightView = CInsightView.of(
                insight.getId(),
                viewCount
        );

        cInsightViewRepository.save(cInsightView);
        log.info("[IDS::incrementViewCount] DB view {}, Cache view {}", viewCount, insight.getView(), cInsightView.getViewCount());
        return viewCount;
    }

    private Long getViewCount(Long insightId) {
       CInsightView insightView = cInsightViewRepository.findById(insightId)
                .orElseGet(() -> {
                    log.info("[IDS::getViewCount] Initialize view count. insightId={}L", insightId);
                    CInsightView dftInsightView = CInsightView.of(insightId, 0L);
                    return dftInsightView;
                });

        return insightView.getViewCount() + 1;
    }

    //FIXME get과 find 역할 정확히 정리하기
    public Insight getById(Long id) {
        return insightRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }
}
