package ccc.keewedomain.service.insight.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CInsightView;
import ccc.keewedomain.cache.repository.insight.CInsightViewRepository;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.InsightDetailDto;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.dto.insight.InsightGetForBookmarkedDto;
import ccc.keewedomain.dto.insight.InsightGetForHomeDto;
import ccc.keewedomain.dto.insight.InsightMyPageDto;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.mysema.commons.lang.Pair;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ReactionRepository reactionRepository;

    public InsightGetDto getInsight(InsightDetailDto detailDto) {
        Insight insight = this.getByIdOrElseThrow(detailDto.getInsightId());
        if (insight.isDeleted()) {
            throw new KeeweException(KeeweRtnConsts.ERR484);
        }
        ReactionAggregationGetDto reactionAggregationGetDto = this.getCurrentReactionAggregation(detailDto.getInsightId());
        List<Reaction> reactions = reactionRepository.findByInsightAndReactor(insight, detailDto.getUser());
        reactionAggregationGetDto.updateClicked(reactions);
        BookmarkId bookmarkId = BookmarkId.of(detailDto.getUserId(), detailDto.getInsightId());
        Long drawerId = insight.getDrawer() != null ? insight.getDrawer().getId() : null;
        String drawerName = drawerId != null ? insight.getDrawer().getName() : "선택안함";
        return InsightGetDto.of(detailDto.getInsightId(), insight.getContents(), insight.getLink(), reactionAggregationGetDto, bookmarkQueryDomainService.isBookmark(bookmarkId), drawerId, drawerName);
    }

    @Transactional(readOnly = true)
    public List<Insight> getAllInsightsByWriterId(Long writerId) {
        return insightQueryRepository.findAllByWriterId(writerId);
    }

    @Transactional(readOnly = true)
    public List<InsightGetForHomeDto> getInsightsForHome(User user, CursorPageable<Long> cPage, Boolean follow) {
        List<Insight> forHome = insightQueryRepository.findAllForHome(user, cPage, follow);
        Map<Long, Boolean> bookmarkPresence = bookmarkQueryDomainService.getBookmarkPresenceMap(user, forHome);

        return forHome.parallelStream().map(i ->
                {
                    ReactionAggregationGetDto reactionAggregation = this.getCurrentReactionAggregation(i.getId());
                    List<Reaction> reactions = reactionRepository.findByInsightAndReactor(i, user);
                    reactionAggregation.updateClicked(reactions);
                    return InsightGetForHomeDto.of(
                            i.getId(),
                            i.getContents(),
                            bookmarkPresence.getOrDefault(i.getId(), false),
                            i.getLink(),
                            reactionAggregation,
                            i.getCreatedAt(),
                            InsightWriterDto.of(
                                    i.getWriter().getId(),
                                    i.getWriter().getNickname(),
                                    i.getWriter().getRepTitleName(),
                                    i.getWriter().getProfilePhotoURL()
                            )
                    );
                }
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
                .map(insight -> {
                    ReactionAggregationGetDto reactionAggregation = this.getCurrentReactionAggregation(insight.getId());
                    List<Reaction> reactions = reactionRepository.findByInsightAndReactor(insight, user);
                    reactionAggregation.updateClicked(reactions);
                    return InsightMyPageDto.of(
                            insight.getId(),
                            insight.getContents(),
                            insight.getLink(),
                            reactionAggregation,
                            insight.getCreatedAt(),
                            bookmarkPresenceMap.getOrDefault(insight.getId(), false)
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InsightGetForBookmarkedDto> getInsightForBookmark(User user, CursorPageable<LocalDateTime> cPage) {
        Map<Insight, LocalDateTime> insights = insightQueryRepository.findBookmarkedInsight(user, cPage);
        Map<Long, ReactionAggregationGetDto> reactionInfo = this.queryReactionAggregationInfo(new ArrayList<>(insights.keySet()), user);
        return insights.entrySet().stream().map(i ->
                InsightGetForBookmarkedDto.of(
                        i.getKey().getId(),
                        i.getKey().getContents(),
                        true,
                        i.getKey().getLink(),
                        reactionInfo.get(i.getKey().getId()),
                        i.getKey().getCreatedAt(),
                        i.getValue(),
                        InsightWriterDto.of(
                                i.getKey().getWriter().getId(),
                                i.getKey().getWriter().getNickname(),
                                i.getKey().getWriter().getRepTitleName(),
                                i.getKey().getWriter().getProfilePhotoURL()
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
                {
                    ReactionAggregationGetDto reactionAggregation = this.getCurrentReactionAggregation(i.getId());
                    List<Reaction> reactions = reactionRepository.findByInsightAndReactor(i, user);
                    reactionAggregation.updateClicked(reactions);
                    return InsightGetForHomeDto.of(
                            i.getId(),
                            i.getContents(),
                            bookmarkPresence.getOrDefault(i.getId(), false),
                            i.getLink(),
                            reactionAggregation,
                            i.getCreatedAt(),
                            InsightWriterDto.of(
                                    i.getWriter().getId(),
                                    i.getWriter().getNickname(),
                                    i.getWriter().getRepTitleName(),
                                    i.getWriter().getProfilePhotoURL()
                            )
                    );
                }
        ).collect(Collectors.toList());
    }

    public Map<Long, Long> getInsightCountPerChallenge(List<Challenge> challenges) {
        return insightQueryRepository.countPerChallenge(challenges);
    }

    public Long getInsightCountByChallenge(Challenge challenge, Long writerId) {
        return insightQueryRepository.countByChallenge(challenge, writerId);
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

    @Transactional(readOnly = true)
    public List<Insight> findAllByUserIdAndDrawerId(Long userId, Long drawerId) {
        return insightQueryRepository.findAllByUserIdAndDrawerId(userId, drawerId);
    }

    // note. <InsightId, ReactionAggregationGetDto>
    private Map<Long, ReactionAggregationGetDto> queryReactionAggregationInfo(List<Insight> insights, User user) {
        return insights.parallelStream()
                .map(insight -> {
                    ReactionAggregationGetDto dto = this.getCurrentReactionAggregation(insight.getId());
                    List<Reaction> reactions = reactionRepository.findByInsightAndReactor(insight, user);
                    dto.updateClicked(reactions);
                    return Pair.of(insight.getId(), dto);
                }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private ReactionAggregationGetDto getCurrentReactionAggregation(Long insightId) {
        return ReactionAggregationGetDto.createByCnt(insightId, cReactionCountRepository.findByIdWithMissHandle(insightId, () ->
                reactionAggregationRepository.findDtoByInsightId(insightId)
        ));
    }

    public void validateWriter(Long userId, Long insightId) {
        Insight insight = this.getByIdOrElseThrow(insightId);
        if(!insight.getWriter().getId().equals(userId)) {
            throw new KeeweException(KeeweRtnConsts.ERR460);
        }
    }
}

