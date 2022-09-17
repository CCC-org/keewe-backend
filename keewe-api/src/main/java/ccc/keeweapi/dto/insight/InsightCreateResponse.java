package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightCreateResponse {
    private Long insightId;

    public static InsightCreateResponse of(Long insightId) {
        InsightCreateResponse response = new InsightCreateResponse();
        response.insightId = insightId;

        return response;
    }
}
