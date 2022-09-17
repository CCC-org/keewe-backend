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
}
