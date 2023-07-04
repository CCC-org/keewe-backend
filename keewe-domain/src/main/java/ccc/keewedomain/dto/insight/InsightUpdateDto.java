package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightUpdateDto {
    private Long userId;
    private Long insightId;
    private String contents;
    private String link;
    private Long drawerId;

    public static InsightUpdateDto of(Long userId, Long insightId, String contents, String link, Long drawerId) {
        InsightUpdateDto dto = new InsightUpdateDto();
        dto.userId = userId;
        dto.insightId = insightId;
        dto.contents = contents;
        dto.link = link;
        dto.drawerId = drawerId;
        return dto;
    }
}
