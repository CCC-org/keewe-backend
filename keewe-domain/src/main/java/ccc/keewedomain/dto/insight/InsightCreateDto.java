package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightCreateDto {
    private Long writerId;
    private String contents;
    private String link;
    private boolean participate;
    private Long drawerId;

    public static InsightCreateDto of(Long writerId, String contents, String link, boolean participate, Long drawerId) {
        InsightCreateDto dto = new InsightCreateDto();
        dto.writerId = writerId;
        dto.contents = contents;
        dto.link = link;
        dto.participate = participate;
        dto.drawerId = drawerId;

        return dto;
    }
}
