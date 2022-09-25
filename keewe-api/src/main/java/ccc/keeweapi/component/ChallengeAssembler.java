package ccc.keeweapi.component;

import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.challenge.Challenge;
import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import org.springframework.stereotype.Component;

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
}
