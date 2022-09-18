package ccc.keeweapi.dto.insight;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightCreateRequest {
    @GraphemeLength(min = 1, max = 300)
    private String contents;

    @Size(min = 1, max = 2000)
    private String link;

    @NotNull
    private boolean participation;

    private Long drawerId;
}
