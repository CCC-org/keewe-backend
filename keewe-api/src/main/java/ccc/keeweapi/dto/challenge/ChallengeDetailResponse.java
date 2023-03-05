package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChallengeDetailResponse {
    private Long challengeId;
    private String challengeName;
    private String challengeCategory;
    private String challengeIntroduction;
    private Long insightCount;
    private String createdAt;
}
