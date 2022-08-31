package ccc.keeweapi.dto.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class ChallengeCreateResponse {
    private Long challengeId;
    private String challengeName;
    private String myTopic;
    private int insightPerWeek;
    private int duration;
    private String endDate;

    public static ChallengeCreateResponse of(Long challengeId, String challengeName, String myTopic, int insightPerWeek, int duration, LocalDate endDate) {
        ChallengeCreateResponse response = new ChallengeCreateResponse();
        response.challengeId = challengeId;
        response.challengeName = challengeName;
        response.myTopic = myTopic;
        response.insightPerWeek = insightPerWeek;
        response.duration = duration;
        response.endDate = endDate.toString();

        return response;
    }
}
