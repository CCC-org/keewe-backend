package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightViewIncrementResponse {
    private Long count;

    public static InsightViewIncrementResponse of(Long count) {
        return new InsightViewIncrementResponse(count);
    }
}
