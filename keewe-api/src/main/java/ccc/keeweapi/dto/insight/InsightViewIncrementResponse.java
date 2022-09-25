package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightViewIncrementResponse {
    private Long viewCount;

    public static InsightViewIncrementResponse of(Long viewCount) {
        return new InsightViewIncrementResponse(viewCount);
    }
}
