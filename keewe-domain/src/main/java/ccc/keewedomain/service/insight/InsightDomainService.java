package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.dto.insight.*;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
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
import ccc.keewedomain.persistence.repository.user.BookmarkQueryRepository;
import ccc.keewedomain.persistence.repository.user.BookmarkRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightDomainService {

    private final InsightRepository insightRepository;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final MQPublishService mqPublishService;
    private final UserDomainService userDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final DrawerDomainService drawerDomainService;
    private final InsightQueryRepository insightQueryRepository;
    private final CInsightViewRepository cInsightViewRepository;
    private final CReactionCountRepository cReactionCountRepository;

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

    public Long delete(Long id) {
        Insight insight = insightRepository.findByIdWithLockOrElseThrow(id);

        // remove relevant data from cache
        cInsightViewRepository.deleteById(id);
        cReactionCountRepository.deleteById(id);

        insight.delete();
        return id;
    }

    public InsightGetDto getInsight(InsightDetailDto detailDto) {
        Insight entity = getByIdOrElseThrow(detailDto.getInsightId());
        ReactionAggregationGetDto reactionAggregationGetDto = getReactionAggregation(detailDto.getInsightId());
        BookmarkId bookmarkId = BookmarkId.of(detailDto.getUserId(), detailDto.getInsightId());

        return InsightGetDto.of(detailDto.getInsightId(), entity.getContents(), entity.getLink(), reactionAggregationGetDto, isBookmark(bookmarkId));
    }

    public List<InsightGetForHomeDto> getInsightsForHome(User user, CursorPageable<Long> cPage, Boolean follow) {
        List<Insight> forHome = insightQueryRepository.findForHome(user, cPage, follow);
        Map<Long, Boolean> bookmarkPresence = bookmarkQueryRepository.getBookmarkPresenceMap(user, forHome);

        return forHome.parallelStream().map(i ->
            InsightGetForHomeDto.of(
                    i.getId(),
                    i.getContents(),
                    bookmarkPresence.getOrDefault(i.getId(), false),
                    i.getLink(),
                    getReactionAggregation(i.getId()),
                    i.getCreatedAt(),
                    InsightWriterDto.of(
                            i.getWriter().getId(),
                            i.getWriter().getNickname(),
                            i.getWriter().getRepTitleName(),
                            i.getWriter().getProfilePhotoURL()
                    )
            )
        ).collect(Collectors.toList());
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
        return insightQueryRepository.countValidByParticipation(participation);
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

        return isBookmark(bookmarkId);
    }

    @Transactional(readOnly = true)
    public Long getRecordOrder(ChallengeParticipation participation, Long insightId) {
        return insightQueryRepository.countValidByIdBeforeAndParticipation(participation, insightId) + 1;
    }

    @Transactional(readOnly = true)
    public Insight getByIdWithChallengeOrElseThrow(Long insightId) {
        return insightQueryRepository.findByIdWithParticipationAndChallenge(insightId)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }

    public boolean isBookmark(BookmarkId bookmarkId) {
        return bookmarkRepository.existsById(bookmarkId);
    }

    @Transactional(readOnly = true)
    public Long countInsightCreatedAtBetween(ChallengeParticipation participation, LocalDateTime startDate, LocalDateTime endDate) {
        return insightQueryRepository.countByParticipationBetween(participation, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<InsightMyPageDto> getInsightsForMyPage(User user, Long targetUserId, Long drawerId, CursorPageable<Long> cPage) {
        List<Insight> insights = insightQueryRepository.findByUserIdAndDrawerId(targetUserId, drawerId, cPage);
        Map<Long, Boolean> bookmarkPresenceMap = bookmarkQueryRepository.getBookmarkPresenceMap(user, insights);
        return insights.parallelStream()
                .map(insight -> InsightMyPageDto.of(
                        insight.getId(),
                        insight.getContents(),
                        insight.getLink(),
                        getReactionAggregation(insight.getId()),
                        insight.getCreatedAt(),
                        bookmarkPresenceMap.getOrDefault(insight.getId(), false)
                ))
                .collect(Collectors.toList());
    }

    public Map<Long, Long> getInsightCountPerChallenge(List<Challenge> challenges) {
        return insightQueryRepository.countPerChallenge(challenges);
    }

    public Long getInsightCountByChallenge(Challenge challenge) {
        return insightQueryRepository.countByChallenge(challenge);
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
        log.info("[IDS::incrementViewCount] DB view {}, Cache view {}", insight.getView(), cInsightView.getViewCount());
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
        cReactionCountRepository.save(CReactionCount.of(insight.getId(), 0L, 0L, 0L, 0L, 0L, 0L));
    }

    private ReactionAggregationGetDto getReactionAggregation(Long insightId) {
        CReactionCount cnt = cReactionCountRepository.findByIdWithMissHandle(insightId, () ->
                reactionAggregationRepository.findDtoByInsightId(insightId)
        );

        return ReactionAggregationGetDto.createByCnt(cnt);
    }

    private boolean isRecordable(ChallengeParticipation participation) {

        if (isTodayRecorded(participation.getChallenger())) {
            return false;
        }
        Long count = getRecordedInsightNumber(participation);
        long weeks = participation.getCurrentWeek();
        log.info("[IDS::isRecordable] count={} weeks={}", count, weeks);
        return count < weeks * participation.getInsightPerWeek();
    }

    private boolean isTodayRecorded(User user) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDate = now.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);
        return insightQueryRepository.existByWriterAndCreatedAtBetweenAndValidTrue(user, startDate, endDate);
    }
}
