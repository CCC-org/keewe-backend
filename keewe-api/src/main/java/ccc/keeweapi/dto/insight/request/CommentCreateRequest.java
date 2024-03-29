package ccc.keeweapi.dto.insight.request;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CommentCreateRequest {

    @NotNull
    private Long insightId;

    private Long parentId;

    @GraphemeLength(min = 1, max = 2000)
    private String content;
}
