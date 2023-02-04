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
import java.util.Objects;


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
    public MyChallengeResponse getMyChallenge() {
        return challengeDomainService.findCurrentChallengeParticipation(SecurityUtil.getUser())
                .map(participation -> {
                    Long participatingUser = challengeDomainService.countParticipatingUser(participation.getChallenge());
                    return challengeAssembler.toMyChallengeResponse(participation, participatingUser);
                })
                .orElse(null);
    }

    private List<String> datesOfWeek(LocalDate startDate) {
        List<String> dates = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            dates.add(startDate.plusDays(i).toString());
        }

        return dates;
    }
}
