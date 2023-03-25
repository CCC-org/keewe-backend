package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ParticipatingChallengeResponse {
    private Long challengeId;
    private String name;
    private String interest;
    private String startDate;
}
