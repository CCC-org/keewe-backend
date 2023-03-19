package ccc.keeweapi.component;

import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
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
                startDate.toString(),
                dayProgresses
        );
    }

    public ParticipatingChallengeResponse toMyChallengeResponse(ChallengeParticipation participation, Long participatingUser) {
        Challenge challenge = participation.getChallenge();
        return ParticipatingChallengeResponse.of(
                challenge.getId(),
                challenge.getName(),
                participatingUser,
                challenge.getInterest().getName(),
                participation.getCreatedAt().toLocalDate().toString()
        );
    }

    public ChallengeInfoResponse toChallengeInfoResponse(Challenge challenge, Long insightCount) {
        return ChallengeInfoResponse.of(challenge.getId(), challenge.getInterest(), challenge.getName(), challenge.getIntroduction(), insightCount);
    }

    public ChallengeHistoryListResponse toChallengeHistoryListResponse(List<ChallengeParticipation> participations, Long historyCount) {
        List<ChallengeHistoryResponse> historyResponses = participations.stream()
                .map(this::toChallengeHistoryResponse)
                .collect(Collectors.toList());

        return ChallengeHistoryListResponse.of(historyCount, historyResponses);
    }

    public ChallengeHistoryResponse toChallengeHistoryResponse(ChallengeParticipation participation) {
        return ChallengeHistoryResponse.of(
                participation.getChallenge().getId(),
                participation.getChallenge().getInterest().getName(),
                participation.getChallenge().getName(),
                participation.getCreatedAt().toLocalDate().toString(),
                participation.getUpdatedAt().toLocalDate().toString()
        );
    }

    public ChallengeDetailResponse toChallengeDetailResponse(Challenge challenge, Long insightCount) {
        return ChallengeDetailResponse.of(
                challenge.getName(),
                challenge.getIntroduction(),
                insightCount,
                challenge.getCreatedAt().toLocalDate().toString()
        );
    }

    public OpenedChallengeResponse toOpenedChallengeResponse(Challenge challenge, Long insightCount) {
        return OpenedChallengeResponse.of(
                challenge.getId(),
                challenge.getInterest().getName(),
                challenge.getIntroduction(),
                challenge.getName(),
                insightCount
        );
    }

    public FinishedChallengeResponse toFinishedChallengeResponse(ChallengeParticipation challengeParticipation) {
        Challenge challenge = challengeParticipation.getChallenge();
        return FinishedChallengeResponse.of(
                challengeParticipation.getId(),
                challenge.getId(),
                challenge.getInterest().getName(),
                challenge.getName(),
                challenge.getCreatedAt().toLocalDate().toString(),
                challengeParticipation.getEndDate().toString()
        );
    }

    public FinishedChallengeCountResponse toFinishedChallengeCountResponse(Long count) {
        return FinishedChallengeCountResponse.of(count);
    }

    public FriendResponse toFriendResponse(ChallengeParticipation participation, Long current, boolean isFollowing) {
        User challenger = participation.getChallenger();
        return FriendResponse.of(
                challenger.getNickname(),
                challenger.getProfilePhotoURL(),
                current,
                participation.getTotalInsightNumber(),
                isFollowing);
    }

    public ChallengerCountResponse toChallengerCountResponse(Long countParticipatingUser) {
        return ChallengerCountResponse.of(countParticipatingUser);
    }

    public ParticipatingChallengeDetailResponse toParticipatingChallengeDetailResponse(Challenge challenge) {
        return ParticipatingChallengeDetailResponse.of(
                challenge.getName(),
                challenge.getInterest().getName(),
                challenge.getIntroduction(),
                challenge.getCreatedAt().toLocalDate().toString()
        );
    }

    public ChallengeInsightNumberResponse toChallengeInsightNumberResponse(Long insightNumber) {
        return ChallengeInsightNumberResponse.of(insightNumber);
    }
}
