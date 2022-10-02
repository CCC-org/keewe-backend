package ccc.keeweapi.dto.insight;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CommentCreateRequest {

    @NotNull
    private Long insightId;

    private Long parentId;

    @GraphemeLength(min = 1, max = 140)
    private String content;
}
