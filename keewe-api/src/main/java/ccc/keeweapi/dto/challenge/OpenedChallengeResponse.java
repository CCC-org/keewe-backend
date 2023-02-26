package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class OpenedChallengeResponse {
    private Long challengeId;
    private String challengeCategory;
    private String challengeName;
    private String startDate;
}