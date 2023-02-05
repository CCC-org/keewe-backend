package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MyChallengeResponse {
    private Long challengeId;
    private String name;
    private Long participatingUserNumber;
    private String interest;
    private String startDate;
}
