package ccc.keeweapi.dto.challenge;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class ChallengeCreateResponse {
    private String challengeName;
    private String myTopic;
    private int insightPerWeek;
    private int duration;
    private LocalDate endDate;

    public static ChallengeCreateResponse of(String challengeName, String myTopic, int insightPerWeek, int duration, LocalDate endDate) {
        ChallengeCreateResponse response = new ChallengeCreateResponse();
        response.challengeName = challengeName;
        response.myTopic = myTopic;
        response.insightPerWeek = insightPerWeek;
        response.duration = duration;
        response.endDate = endDate;

        return response;
    }
}
