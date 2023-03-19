package ccc.keewedomain.service.insight.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.InsightDetailDto;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.dto.insight.InsightGetForHomeDto;
import ccc.keewedomain.dto.insight.InsightMyPageDto;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class InsightQueryDomainService {
    private final InsightRepository insightRepository;
    private final InsightQueryRepository insightQueryRepository;
    private final BookmarkQueryDomainService bookmarkQueryDomainService;
    private final CInsightViewRepository cInsightViewRepository;
    private final CReactionCountRepository cReactionCountRepository;
    private final ReactionAggregationRepository reactionAggregationRepository;

    public InsightGetDto getInsight(InsightDetailDto detailDto) {
        Insight entity = getByIdOrElseThrow(detailDto.getInsightId());
        ReactionAggregationGetDto reactionAggregationGetDto = this.getCurrentReactionAggregation(detailDto.getInsightId());
        BookmarkId bookmarkId = BookmarkId.of(detailDto.getUserId(), detailDto.getInsightId());

        return InsightGetDto.of(detailDto.getInsightId(), entity.getContents(), entity.getLink(), reactionAggregationGetDto, bookmarkQueryDomainService.isBookmark(bookmarkId));
    }

    public List<InsightGetForHomeDto> getInsightsForHome(User user, CursorPageable<Long> cPage, Boolean follow) {
        List<Insight> forHome = insightQueryRepository.findAllForHome(user, cPage, follow);
        Map<Long, Boolean> bookmarkPresence = bookmarkQueryDomainService.getBookmarkPresenceMap(user, forHome);

        return forHome.parallelStream().map(i ->
                InsightGetForHomeDto.of(
                        i.getId(),
                        i.getContents(),
                        bookmarkPresence.getOrDefault(i.getId(), false),
                        i.getLink(),
                        this.getCurrentReactionAggregation(i.getId()),
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

    public List<Insight> getRecordedInsights(ChallengeParticipation challengeParticipation) {
        return insightQueryRepository.findAllValidByParticipation(challengeParticipation);
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

    @Transactional(readOnly = true)
    public Long countInsightCreatedAtBetween(ChallengeParticipation participation, LocalDateTime startDate, LocalDateTime endDate) {
        return insightQueryRepository.countByParticipationBetween(participation, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<InsightMyPageDto> getInsightsForMyPage(User user, Long targetUserId, Long drawerId, CursorPageable<Long> cPage) {
        List<Insight> insights = insightQueryRepository.findAllByUserIdAndDrawerId(targetUserId, drawerId, cPage);
        Map<Long, Boolean> bookmarkPresenceMap = bookmarkQueryDomainService.getBookmarkPresenceMap(user, insights);
        return insights.parallelStream()
                .map(insight -> InsightMyPageDto.of(
                        insight.getId(),
                        insight.getContents(),
                        insight.getLink(),
                        this.getCurrentReactionAggregation(insight.getId()),
                        insight.getCreatedAt(),
                        bookmarkPresenceMap.getOrDefault(insight.getId(), false)
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsightGetForHomeDto> getInsightForBookmark(User user, CursorPageable<Long> cPage) {
        List<Insight> insights = insightQueryRepository.findBookmarkedInsight(user, cPage);
        return insights.parallelStream().map(i ->
                InsightGetForHomeDto.of(
                        i.getId(),
                        i.getContents(),
                        true,
                        i.getLink(),
                        this.getCurrentReactionAggregation(i.getId()),
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

    // FIXME dto 개선 필요
    @Transactional(readOnly = true)
    public List<InsightGetForHomeDto> getByChallenge(Challenge challenge, User user, CursorPageable<Long> cPage, Long writerId) {
        List<Insight> insights = insightQueryRepository.findByChallenge(challenge, cPage, writerId);
        Map<Long, Boolean> bookmarkPresence = bookmarkQueryDomainService.getBookmarkPresenceMap(user, insights);

        return insights.parallelStream().map(i ->
                InsightGetForHomeDto.of(
                        i.getId(),
                        i.getContents(),
                        bookmarkPresence.getOrDefault(i.getId(), false),
                        i.getLink(),
                        this.getCurrentReactionAggregation(i.getId()),
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

    public Map<Long, Long> getInsightCountPerChallenge(List<Challenge> challenges) {
        return insightQueryRepository.countPerChallenge(challenges);
    }

    public Long getInsightCountByChallenge(Challenge challenge) {
        return insightQueryRepository.countByChallenge(challenge);
    }

    public Map<Long, Long> getInsightCountPerParticipation(List<ChallengeParticipation> participations) {
        return insightQueryRepository.countValidPerParticipation(participations);
    }

    public Insight getByIdWithWriter(Long insightId) {
        return insightQueryRepository.findByIdWithWriter(insightId);
    }

    public Insight getByIdOrElseThrow(Long id) {
        return insightRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }

    public Long getRecordedInsightNumber(ChallengeParticipation participation) {
        return insightQueryRepository.countValidByParticipation(participation);
    }

    public Long getViewCount(Long insightId) {
        CInsightView insightView = cInsightViewRepository.findById(insightId)
                .orElseGet(() -> {
                    log.info("[IDS::getViewCount] Initialize view count. insightId={}L", insightId);
                    CInsightView dftInsightView = CInsightView.of(insightId, 0L);
                    return dftInsightView;
                });

        return insightView.getViewCount() + 1;
    }

    public boolean isRecordable(ChallengeParticipation participation) {
        if (isTodayRecorded(participation.getChallenger())) {
            return false;
        }
        Long count = getRecordedInsightNumber(participation);
        long weeks = participation.getCurrentWeek();
        log.info("[IDS::isRecordable] count={} weeks={}", count, weeks);
        return count < weeks * participation.getInsightPerWeek();
    }

    public boolean isTodayRecorded(User user) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDate = now.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);
        return insightQueryRepository.existByWriterAndCreatedAtBetweenAndValidTrue(user, startDate, endDate);
    }

    public boolean isThisWeekCompleted(ChallengeParticipation participation) {
        Long validNumber = insightQueryRepository.countValidByParticipation(participation);
        log.info("[IQDS::isThisWeekCompleted] validNumber = {}, currentWeek = {}, insightPerWeek = {}", validNumber, participation.getCurrentWeek(), participation.getInsightPerWeek());
        return validNumber.equals(participation.getCurrentWeek() * participation.getInsightPerWeek());
    }

    private ReactionAggregationGetDto getCurrentReactionAggregation(Long insightId) {
        return ReactionAggregationGetDto.createByCnt(cReactionCountRepository.findByIdWithMissHandle(insightId, () ->
                reactionAggregationRepository.findDtoByInsightId(insightId)
        ));
    }
}

