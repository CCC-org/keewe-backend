package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightCreateResponse {
    private Long insightId;

    public static InsightCreateResponse of(Long insightId) {
        InsightCreateResponse response = new InsightCreateResponse();
        response.insightId = insightId;

        return response;
    }
}
