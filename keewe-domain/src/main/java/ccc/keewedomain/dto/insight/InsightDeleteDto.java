package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightDeleteDto {

    private Long insightId;
    private Long writerId;

    public static InsightDeleteDto of(Long insightId, Long writerId) {
        InsightDeleteDto dto = new InsightDeleteDto();
        dto.insightId = insightId;
        dto.writerId = writerId;
        return dto;
    }
}
