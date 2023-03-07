package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ParticipatingChallengeDetailResponse {
    private String challengeName;
    private String challengeCategory;
    private String challengeIntroduction;
    private String createdAt;
}
