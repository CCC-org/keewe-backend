package ccc.keewedomain.service.insight.command;

import static ccc.keewecore.consts.KeeweRtnConsts.ERR447;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.InsightDeleteDto;
import ccc.keewedomain.dto.insight.InsightUpdateDto;
import ccc.keewedomain.dto.insight.InsightViewIncrementDto;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.insight.DrawerDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class InsightCommandDomainService {
    private final InsightRepository insightRepository;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final MQPublishService mqPublishService;
    private final UserDomainService userDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final DrawerDomainService drawerDomainService;
    private final CInsightViewRepository cInsightViewRepository;
    private final CReactionCountRepository cReactionCountRepository;

    @Transactional
    public Insight create(InsightCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());
        Drawer drawer = drawerDomainService.getDrawerIfOwner(dto.getDrawerId(), writer);
        ChallengeParticipation participation = null;
        boolean valid = false;
        if (dto.isParticipate()) {
            participation = challengeParticipateQueryDomainService.getCurrentChallengeParticipation(writer);
            valid = insightQueryDomainService.isRecordable(participation);
        }

        Insight insight = Insight.of(writer, participation, drawer, dto.getContents(), Link.of(dto.getLink()), valid);
        insightRepository.save(insight);
        createReactionAggregations(insight);
        return insight;
    }

    @Transactional
    public Insight update(InsightUpdateDto dto) {
        Insight insight = insightQueryDomainService.getByIdOrElseThrow(dto.getInsightId());
        Long writerId = insight.getWriter().getId();
        Drawer drawer = drawerDomainService.getDrawerIfOwner(dto.getDrawerId(), insight.getWriter());
        assert writerId.equals(dto.getInsightId());
        insight.update(dto.getContents(), Link.of(dto.getLink()), drawer);
        return insightRepository.save(insight);
    }

    @Transactional
    public Long delete(InsightDeleteDto dto) {
        Long writerId = dto.getWriterId();
        Long insightId = dto.getInsightId();
        Insight insight = insightRepository.findByIdWithLockOrElseThrow(insightId);

        if (!Objects.equals(writerId, insight.getWriter().getId()))
            throw new KeeweException(ERR447);

        // remove relevant data from cache
        cInsightViewRepository.deleteById(insightId);
        cReactionCountRepository.deleteById(insightId);

        insight.delete();
        return insightId;
    }

    @Transactional
    public void deleteAll(Long writerId) {
        List<Insight> insights = insightQueryDomainService.getAllInsightsByWriterId(writerId);
        insights.forEach(Insight::delete);
        insightRepository.saveAll(insights);
    }

    public Long incrementViewCount(InsightViewIncrementDto dto) {
        // 캐시 생성부터. 생성과 조회수 + 1이 역전될수있음
        Long viewCount = insightQueryDomainService.getViewCount(dto.getInsightId());

        log.info("[InsightCommandDomainService] 인사이트 조회수 증가 이벤트 생성 - insightId({}), cacheViewCount({})", dto.getInsightId(), viewCount);
        mqPublishService.publish(KeeweConsts.INSIGHT_VIEW_EXCHANGE, String.valueOf(dto.getInsightId()));

        return viewCount;
    }

    @Transactional
    public Long incrementViewCount(Long insightId) {
        Insight insight = insightRepository.findByIdWithLockOrElseThrow(insightId);
        return incrementViewCount(insight);
    }

    private void createReactionAggregations(Insight insight) {
        Arrays.stream(ReactionType.values()).forEach((reactionType) -> {
            reactionAggregationRepository.save(ReactionAggregation.of(insight, reactionType, 0L));
        });
        cReactionCountRepository.save(CReactionCount.of(insight.getId(), 0L, 0L, 0L, 0L, 0L, 0L));
    }

    private Long incrementViewCount(Insight insight) {
        Long viewCount = insight.incrementView();

        CInsightView cInsightView = CInsightView.of(
                insight.getId(),
                viewCount
        );

        cInsightViewRepository.save(cInsightView);
        log.info("[InsightCommandDomainService] 인사이트 조회수 증가 처리 - insightId({}), dbViewCount({}), cacheViewCount({})", insight.getId(), insight.getView(), cInsightView.getViewCount());
        return viewCount;
    }
}
