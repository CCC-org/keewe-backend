package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.PostContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePostCreateRequest {
    private Long profileId;
    @Size(min = 2, max = 5)
    private List<String> candidates;
    @PostContent
    private String content;
}
