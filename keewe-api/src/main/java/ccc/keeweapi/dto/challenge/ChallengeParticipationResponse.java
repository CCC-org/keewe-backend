package ccc.keeweapi.dto.challenge;

import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class ChallengeParticipationResponse {
    private String myTopic;
    private int insightPerWeek;
    private int duration;
    private String endDate;
    
    public static ChallengeParticipationResponse of(String myTopic, int insightPerWeek, int duration, LocalDate endDate) {
        ChallengeParticipationResponse response = new ChallengeParticipationResponse();
        response.myTopic = myTopic;
        response.insightPerWeek = insightPerWeek;
        response.duration = duration;
        response.endDate = endDate.toString();
        return response;
    }
}
