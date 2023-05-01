package ccc.keeweapi.dto.insight.request;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightUpdateRequest {
    @NotNull
    private Long insightId;

    @GraphemeLength(min = 1, max = 400)
    private String contents;

    @Size(min = 1, max = 2000)
    private String link;
}
