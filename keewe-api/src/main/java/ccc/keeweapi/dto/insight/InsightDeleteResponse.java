package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightDeleteResponse {
    private Long insightId;

    public static InsightDeleteResponse of(Long insightId) {
        InsightDeleteResponse response = new InsightDeleteResponse();
        response.insightId = insightId;
        return response;
    }
}
