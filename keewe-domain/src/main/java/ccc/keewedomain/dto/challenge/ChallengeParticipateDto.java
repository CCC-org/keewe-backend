package ccc.keewedomain.dto.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChallengeParticipateDto {
    private Long challengeId;
    private Long challengerId;
    private String myTopic;
    private int insightPerWeek;
    private int duration;

    public static ChallengeParticipateDto of(Long challengeId, Long challengerId, String myTopic, int insightPerWeek, int duration) {
        ChallengeParticipateDto dto = new ChallengeParticipateDto();
        dto.challengeId = challengeId;
        dto.challengerId = challengerId;
        dto.myTopic = myTopic;
        dto.insightPerWeek = insightPerWeek;
        dto.duration = duration;

        return dto;
    }
}
