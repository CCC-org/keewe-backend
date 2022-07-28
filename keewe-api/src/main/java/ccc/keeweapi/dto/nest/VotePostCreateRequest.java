package ccc.keeweapi.dto.nest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(as = VotePostCreateRequest.class)
public class VotePostCreateRequest implements AbstractPostCreateRequest {
    private Long profileId;
    //  @PostContent
    private String content;

    @NotBlank
    private String postType;

    @Size(min = 2, max = 5)
    private List<String> candidates;
}
