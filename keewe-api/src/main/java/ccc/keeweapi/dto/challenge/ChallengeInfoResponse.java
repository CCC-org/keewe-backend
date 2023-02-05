package ccc.keeweapi.dto.challenge;

import static lombok.AccessLevel.PRIVATE;

import ccc.keewedomain.persistence.domain.common.Interest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
@Getter
public class ChallengeInfoResponse {
    private Long challengeId;
    private String challengeCategory;
    private String challengeName;
    private String challengeIntroduction;
    private Long insightCount;

    public static ChallengeInfoResponse of(Long id, Interest interest, String name, String introduction, Long insightCount) {
        ChallengeInfoResponse response = new ChallengeInfoResponse();
        response.challengeId = id;
        response.challengeCategory = interest.getName();
        response.challengeName = name;
        response.challengeIntroduction = introduction;
        response.insightCount = insightCount;
        return response;
    }
}
