package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightCreateRequest {
    private String contents;
    private String link;
    private Long drawerId;
}
