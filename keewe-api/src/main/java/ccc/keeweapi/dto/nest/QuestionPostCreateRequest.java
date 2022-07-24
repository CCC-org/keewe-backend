package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.annotations.PostContent;
import lombok.Data;

@Data
public class QuestionPostCreateRequest {
    private Long profileId;
    @PostContent
    private String content;
}
