package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.domain.insight.id.CReactionCountId;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.dto.insight.*;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.insight.Bookmark;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.user.BookmarkRepository;
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
    private final BookmarkRepository bookmarkRepository;
    private final MQPublishService mqPublishService;
    private final UserDomainService userDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final DrawerDomainService drawerDomainService;
    private final InsightQueryRepository insightQueryRepository;
    private final CInsightViewRepository cInsightViewRepository;
    private final CReactionCountRepository cReactionCountRepository;

    //TODO 참가한 챌린지에 기록하기
    public Insight create(InsightCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());
        Drawer drawer = drawerDomainService.getDrawerIfOwner(dto.getDrawerId(), writer);
        ChallengeParticipation participation = null;
        boolean valid = false;
        if (dto.isParticipate()) {
            participation = challengeDomainService.getCurrentChallengeParticipation(writer);
            valid = isRecordable(participation);
        }

        Insight insight = Insight.of(writer, participation, drawer, dto.getContents(), Link.of(dto.getLink()), valid);
        insightRepository.save(insight);
        createReactionAggregations(insight);
        return insight;
    }

    public InsightGetDto getInsight(Long insightId) {
        Insight entity = getByIdOrElseThrow(insightId);
        ReactionAggregationGetDto reactionAggregationGetDto = getReactionAggregation(insightId);

        return InsightGetDto.of(insightId, entity.getContents(), entity.getLink(), reactionAggregationGetDto);
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
        Insight insight = insightRepository.findByIdWithLockOrElseThrow(insightId);
        log.info("[IDS::incrementViewCount] DB Curr view {}, Next view {}", insight.getView(), insight.getView() + 1);
        return incrementViewCount(insight);
    }

    public Insight getByIdWithWriter(Long insightId) {
        return insightQueryRepository.findByIdWithWriter(insightId);
    }

    //FIXME get과 find 역할 정확히 정리하기
    public Insight getByIdOrElseThrow(Long id) {
        return insightRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }

    public Long getRecordedInsightNumber(ChallengeParticipation participation) {
        return insightQueryRepository.countValidForParticipation(participation);
    }

    @Transactional
    public boolean toggleInsightBookmark(BookmarkToggleDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Insight insight = this.getByIdOrElseThrow(dto.getInsightId());

        BookmarkId bookmarkId = BookmarkId.of(dto.getUserId(), dto.getInsightId());

        bookmarkRepository.findById(bookmarkId)
                .ifPresentOrElse(
                        bookmark -> {
                            log.info("[IDS::toggleInsightBookmark] Found Bookmark user {}, insight {}", bookmarkId.getUser(), bookmarkId.getInsight());
                            bookmarkRepository.delete(bookmark);
                        },
                        () -> {
                            log.info("[IDS::toggleInsightBookmark] Not Found Bookmark user {}, insight {}", bookmarkId.getUser(), bookmarkId.getInsight());
                            bookmarkRepository.save(Bookmark.of(user, insight));
                        }
                );

        return bookmarkRepository.existsById(bookmarkId);
    }


    /*****************************************************************
     ********************** private 메소드 영역 분리 *********************
     *****************************************************************/

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

    private void createReactionAggregations(Insight insight) {
        Arrays.stream(ReactionType.values()).forEach((reactionType) -> {
            reactionAggregationRepository.save(ReactionAggregation.of(insight, reactionType, 0L));
        });
    }

    private ReactionAggregationGetDto getReactionAggregation(Long insightId) {
        Long clap = 0L, heart = 0L, sad = 0L, surprise = 0L, fire = 0L, eyes = 0L;
        for (ReactionType r : ReactionType.values()) {
            String id = new CReactionCountId(insightId, r).toString();
            Long count = cReactionCountRepository.findById(id).orElseGet(() -> CReactionCount.of(id, 0L)).getCount();

            switch (r) {
                case CLAP: clap = count;
                case HEART: heart = count;
                case SAD: sad = count;
                case SURPRISE: surprise = count;
                case FIRE: fire = count;
                case EYES: eyes = count;
            }

        }

        return ReactionAggregationGetDto.of(clap, heart, sad, surprise, fire, eyes);
    }

    private boolean isRecordable(ChallengeParticipation participation) {
        Long count = getRecordedInsightNumber(participation);
        long weeks = participation.getCurrentWeek();
        log.info("[IDS::isRecordable] count={} weeks={}", count, weeks);
        return count < weeks * participation.getInsightPerWeek();
    }
}
