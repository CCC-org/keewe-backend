package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChallengeRecordResponse {

    private Long challengeId;
    private String challengeName;
    private Long order;
    private Long total;
}
