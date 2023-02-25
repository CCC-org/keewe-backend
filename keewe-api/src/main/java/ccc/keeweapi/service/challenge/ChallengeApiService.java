package ccc.keeweapi.service.challenge;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeApiService {
    private final ChallengeDomainService challengeDomainService;
    private final InsightDomainService insightDomainService;
    private final ChallengeAssembler challengeAssembler;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request) {
        Challenge challenge = challengeDomainService.save(challengeAssembler.toChallengeCreateDto(request));
        ChallengeParticipation participation = challengeDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request.getParticipate(), challenge.getId()));
        return challengeAssembler.toChallengeCreateResponse(challenge, participation);
    }

    @Transactional
    public ChallengeParticipationResponse participate(ChallengeParticipateRequest request) {
        ChallengeParticipation participation = challengeDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request));
        return challengeAssembler.toChallengeParticipationResponse(participation);
    }

    @Transactional(readOnly = true)
    public ParticipationCheckResponse checkParticipation() {
        boolean participation = challengeDomainService.checkParticipation(SecurityUtil.getUserId());
        return challengeAssembler.toParticipationCheckResponse(participation);
    }

    @Transactional(readOnly = true)
    public InsightProgressResponse getMyParticipationProgress() {
        Long userId = SecurityUtil.getUserId();

        return challengeDomainService.findCurrentParticipationWithChallenge(userId)
                .map(participation -> {
                    Long current = insightDomainService.getRecordedInsightNumber(participation);
                    return challengeAssembler.toParticipationProgressResponse(participation, current);
                })
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public WeekProgressResponse getWeekProgress() {
        return challengeDomainService.findCurrentChallengeParticipation(SecurityUtil.getUser())
                .map(participation -> {
                    Map<String, Long> recordCountPerDate = challengeDomainService.getRecordCountPerDate(participation);
                    LocalDate startDateOfWeek = participation.getStartDateOfThisWeek();
                    List<String> dates = datesOfWeek(startDateOfWeek);
                    return challengeAssembler.toWeekProgressResponse(dates, recordCountPerDate, participation, startDateOfWeek);
                })
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ParticipatingChallengeResponse getParticipatingChallenege() {
        return challengeDomainService.findCurrentChallengeParticipation(SecurityUtil.getUser())
                .map(participation -> {
                    Long participatingUser = challengeDomainService.countParticipatingUser(participation.getChallenge());
                    return challengeAssembler.toMyChallengeResponse(participation, participatingUser);
                })
                .orElse(null);
    }

    public List<ChallengeInfoResponse> getSpecifiedNumberOfChallenge(int size) {
        List<Challenge> specifiedNumberOfChallenge = challengeDomainService.getSpecifiedNumberOfRecentChallenge(size);
        Map<Long, Long> insightCountPerChallengeMap = insightDomainService.getInsightCountPerChallenge(specifiedNumberOfChallenge);
        return specifiedNumberOfChallenge.stream()
                .map(challenge -> challengeAssembler.toChallengeInfoResponse(challenge, insightCountPerChallengeMap.getOrDefault(challenge.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public ChallengeHistoryListResponse getHistoryOfChallenge(Long size) {
        User user = SecurityUtil.getUser();
        Long historyCount = challengeDomainService.countFinishedParticipation(user);
        List<ChallengeParticipation> finishedParticipation = challengeDomainService.getFinishedParticipation(user, size);

        return challengeAssembler.toChallengeHistoryListResponse(finishedParticipation, historyCount);
    }

    public ChallengeDetailResponse getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeDomainService.getByIdOrElseThrow(challengeId);
        Long insightCount = insightDomainService.getInsightCountByChallenge(challenge);
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
    public List<TogetherChallengerResponse> getTogetherChallengers(Long challengeId) {
        User user = SecurityUtil.getUser();
        Challenge challenge = challengeDomainService.getByIdOrElseThrow(challengeId);
        List<ChallengeParticipation> participations = challengeDomainService.findTogetherChallengeParticipations(challenge, user);
        Map<Long, Long> insightCountPerParticipation = insightDomainService.getInsightCountPerParticipation(participations);

        return participations.stream()
                .map(participation -> challengeAssembler.toTogetherChallengerResponse(
                        participation,
                        insightCountPerParticipation.getOrDefault(participation.getId(), 0L))
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChallengerCountResponse getChallengerCount(Long challengeId) {
        Challenge challenge = challengeDomainService.getByIdOrElseThrow(challengeId);
        return challengeAssembler.toChallengerCountResponse(challengeDomainService.countParticipatingUser(challenge));
    }
}
