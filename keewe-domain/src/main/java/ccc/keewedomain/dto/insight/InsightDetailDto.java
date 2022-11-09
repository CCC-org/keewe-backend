package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightDetailDto {
    private Long userId;
    private Long insightId;

    public static InsightDetailDto of(Long userId, Long insightId) {
        InsightDetailDto dto = new InsightDetailDto();
        dto.userId = userId;
        dto.insightId = insightId;
        return dto;
    }
}
