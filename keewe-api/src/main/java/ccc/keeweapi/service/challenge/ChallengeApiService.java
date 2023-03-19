package ccc.keeweapi.service.challenge;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.ChallengeCreateRequest;
import ccc.keeweapi.dto.challenge.ChallengeCreateResponse;
import ccc.keeweapi.dto.challenge.ChallengeDetailResponse;
import ccc.keeweapi.dto.challenge.ChallengeHistoryListResponse;
import ccc.keeweapi.dto.challenge.ChallengeInfoResponse;
import ccc.keeweapi.dto.challenge.ChallengeParticipateRequest;
import ccc.keeweapi.dto.challenge.ChallengeParticipationResponse;
import ccc.keeweapi.dto.challenge.ChallengerCountResponse;
import ccc.keeweapi.dto.challenge.MyParticipationProgressResponse;
import ccc.keeweapi.dto.challenge.ParticipatingChallengeDetailResponse;
import ccc.keeweapi.dto.challenge.ParticipatingChallengeResponse;
import ccc.keeweapi.dto.challenge.ParticipationCheckResponse;
import ccc.keeweapi.dto.challenge.FriendResponse;
import ccc.keeweapi.dto.challenge.WeekProgressResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.challenge.command.ChallengeCommandDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeApiService {

    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final ChallengeCommandDomainService challengeCommandDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ProfileDomainService profileDomainService;
    private final ChallengeAssembler challengeAssembler;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request) {
        Challenge challenge = challengeCommandDomainService.save(challengeAssembler.toChallengeCreateDto(request));
        ChallengeParticipation participation = challengeCommandDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request.getParticipate(), challenge.getId()));
        return challengeAssembler.toChallengeCreateResponse(challenge, participation);
    }

    @Transactional
    public ChallengeParticipationResponse participate(ChallengeParticipateRequest request) {
        ChallengeParticipation participation = challengeCommandDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request));
        return challengeAssembler.toChallengeParticipationResponse(participation);
    }

    @Transactional(readOnly = true)
    public ParticipationCheckResponse checkParticipation() {
        boolean participation = challengeParticipateQueryDomainService.checkParticipation(SecurityUtil.getUserId());
        return challengeAssembler.toParticipationCheckResponse(participation);
    }

    @Transactional(readOnly = true)
    public MyParticipationProgressResponse getMyParticipationProgress() {
        User user = SecurityUtil.getUser();

        return challengeParticipateQueryDomainService.findCurrentParticipationWithChallenge(user.getId())
                .map(participation -> {
                    Long current = insightQueryDomainService.getRecordedInsightNumber(participation);
                    boolean todayRecorded = insightQueryDomainService.isTodayRecorded(user);
                    boolean weekCompleted =  insightQueryDomainService.isThisWeekCompleted(participation);
                    return challengeAssembler.toMyParticipationProgressResponse(participation, current, todayRecorded, weekCompleted);
                })
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public WeekProgressResponse getWeekProgress() {
        return challengeParticipateQueryDomainService.findCurrentChallengeParticipation(SecurityUtil.getUser())
                .map(participation -> {
                    Map<String, Long> recordCountPerDate = challengeParticipateQueryDomainService.getRecordCountPerDate(participation);
                    LocalDate startDateOfWeek = participation.getStartDateOfThisWeek();
                    List<String> dates = datesOfWeek(startDateOfWeek);
                    return challengeAssembler.toWeekProgressResponse(dates, recordCountPerDate, participation, startDateOfWeek);
                })
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ParticipatingChallengeResponse getParticipatingChallenege() {
        return challengeParticipateQueryDomainService.findCurrentChallengeParticipation(SecurityUtil.getUser())
                .map(participation -> {
                    Long participatingUser = challengeParticipateQueryDomainService.countParticipatingUser(participation.getChallenge());
                    return challengeAssembler.toMyChallengeResponse(participation, participatingUser);
                })
                .orElse(null);
    }

    public List<ChallengeInfoResponse> getSpecifiedNumberOfChallenge(int size) {
        List<Challenge> specifiedNumberOfChallenge = challengeQueryDomainService.getSpecifiedNumberOfRecentChallenge(size);
        Map<Long, Long> insightCountPerChallengeMap = insightQueryDomainService.getInsightCountPerChallenge(specifiedNumberOfChallenge);
        return specifiedNumberOfChallenge.stream()
                .map(challenge -> challengeAssembler.toChallengeInfoResponse(challenge, insightCountPerChallengeMap.getOrDefault(challenge.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public ChallengeHistoryListResponse getHistoryOfChallenge(Long size) {
        User user = SecurityUtil.getUser();
        Long historyCount = challengeParticipateQueryDomainService.countFinishedParticipation(user);
        List<ChallengeParticipation> finishedParticipation = challengeParticipateQueryDomainService.getFinishedParticipation(user, size);

        return challengeAssembler.toChallengeHistoryListResponse(finishedParticipation, historyCount);
    }

    public ChallengeDetailResponse getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(challengeId);
        Long insightCount = insightQueryDomainService.getInsightCountByChallenge(challenge);
        return challengeAssembler.toChallengeDetailResponse(challenge, insightCount);
    }

    private List<String> datesOfWeek(LocalDate startDate) {
        List<String> dates = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            dates.add(startDate.plusDays(i).toString());
        }

        return dates;
    }

    @Transactional(readOnly = true)
    public List<FriendResponse> paginateFriends(Long challengeId, Pageable pageable) {
        User user = SecurityUtil.getUser();
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(challengeId);
        List<ChallengeParticipation> participations = challengeParticipateQueryDomainService.findFriendsParticipations(challenge, user, pageable);
        if(participations.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> challengers = participations.stream().map(ChallengeParticipation::getChallenger).collect(Collectors.toList());
        Set<Long> followingIdSet = profileDomainService.getFollowingTargetIdSet(SecurityUtil.getUser(), challengers);
        Map<Long, Long> insightCountPerParticipation = insightQueryDomainService.getInsightCountPerParticipation(participations);

        return participations.stream()
                .map(participation -> challengeAssembler.toFriendResponse(
                        participation,
                        insightCountPerParticipation.getOrDefault(participation.getId(), 0L),
                        followingIdSet.contains(participation.getChallenger().getId()))
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChallengerCountResponse getChallengerCount(Long challengeId) {
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(challengeId);
        return challengeAssembler.toChallengerCountResponse(challengeParticipateQueryDomainService.countParticipatingUser(challenge));
    }

    public ParticipatingChallengeDetailResponse getMyChallengeDetail() {
        Challenge challenge = challengeParticipateQueryDomainService.findCurrentParticipationWithChallenge(SecurityUtil.getUserId())
                .map(ChallengeParticipation::getChallenge)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));
        return challengeAssembler.toParticipatingChallengeDetailResponse(challenge);
    }
}
