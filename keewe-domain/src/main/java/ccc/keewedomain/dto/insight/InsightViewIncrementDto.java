package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightViewIncrementDto {
    private Long insightId;

    public static InsightViewIncrementDto of(Long insightId) {
        return new InsightViewIncrementDto(insightId);
    }
}
