package ccc.keeweapi.dto.insight.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightUpdateResponse {
    private Long insightId;

    public static InsightUpdateResponse of(Long insightId) {
        InsightUpdateResponse response =  new InsightUpdateResponse();
        response.insightId = insightId;
        return response;
    }
}
