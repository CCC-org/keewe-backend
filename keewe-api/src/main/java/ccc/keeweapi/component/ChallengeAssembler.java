package ccc.keeweapi.component;

import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChallengeAssembler {
    public ChallengeCreateResponse toChallengeCreateResponse(Challenge challenge, ChallengeParticipation participation) {
        return ChallengeCreateResponse.of(
                challenge.getId(),
                challenge.getName(),
                participation.getMyTopic(),
                participation.getInsightPerWeek(),
                participation.getDuration(),
                participation.getEndDate()
        );
    }

    public ChallengeParticipationResponse toChallengeParticipationResponse(ChallengeParticipation participation) {
        return ChallengeParticipationResponse.of(
                participation.getMyTopic(),
                participation.getInsightPerWeek(),
                participation.getDuration(),
                participation.getEndDate()
        );
    }

    public ChallengeCreateDto toChallengeCreateDto(ChallengeCreateRequest request) {
        return ChallengeCreateDto.of(SecurityUtil.getUserId(), request.getName(), request.getInterest(), request.getIntroduction());
    }

    public ChallengeParticipateDto toChallengeParticipateDto(ChallengeParticipateRequest request) {
        return ChallengeParticipateDto.of(
                request.getChallengeId(),
                SecurityUtil.getUserId(),
                request.getMyTopic(),
                request.getInsightPerWeek(),
                request.getDuration());
    }

    public ChallengeParticipateDto toChallengeParticipateDto(ChallengeParticipateRequest request, Long challengeId) {
        return ChallengeParticipateDto.of(
                challengeId,
                SecurityUtil.getUserId(),
                request.getMyTopic(),
                request.getInsightPerWeek(),
                request.getDuration());
    }

    public ParticipationCheckResponse toParticipationCheckResponse(boolean participation) {
        return ParticipationCheckResponse.of(participation);
    }

    public InsightProgressResponse toParticipationProgressResponse(ChallengeParticipation participation, Long current) {
        return InsightProgressResponse.of(
                participation.getChallenge().getName(),
                current,
                participation.getTotalInsightNumber()
        );
    }

    public WeekProgressResponse toWeekProgressResponse(
            List<String> dates,
            Map<String, Long> recordCountPerDate,
            ChallengeParticipation participation,
            LocalDate startDate) {

        List<DayProgressResponse> dayProgresses = dates.stream()
                .map(recordCountPerDate::containsKey)
                .map(DayProgressResponse::of)
                .collect(Collectors.toList());

        long recorded = recordCountPerDate.values().stream()
                .mapToLong(v -> v)
                .sum();

        Challenge challenge = participation.getChallenge();

        return WeekProgressResponse.of(challenge.getId(),
                participation.getInsightPerWeek() - recorded,
                challenge.getName(),
                startDate,
                dayProgresses
        );
    }
}
