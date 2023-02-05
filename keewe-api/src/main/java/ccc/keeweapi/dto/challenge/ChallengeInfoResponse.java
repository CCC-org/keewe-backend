package ccc.keeweapi.dto.challenge;

import static lombok.AccessLevel.PROTECTED;

import ccc.keewedomain.persistence.domain.common.Interest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class ChallengeInfoResponse {
    private Long id;
    private Interest interest;
    private String name;
    private String introduction;
    private Long insightCount;

    public static ChallengeInfoResponse of(Long id, Interest interest, String name, String introduction, Long insightCount) {
        ChallengeInfoResponse response = new ChallengeInfoResponse();
        response.id = id;
        response.interest = interest;
        response.name = name;
        response.introduction = introduction;
        response.insightCount = insightCount;
        return response;
    }
}
