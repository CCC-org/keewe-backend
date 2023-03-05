package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CompletedChallengeResponse {
    private Long challengeParticipationId;
    private Long challengeId;
    private String challengeCategory;
    private String challengeName;
    private String startDate;
    private String endDate;
}
