package ccc.keeweapi.dto.nest;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("VOTE")
public class VotePostCreateRequest extends PostCreateRequest {
    @Size(min = 2, max = 5)
    private List<String> candidates;
}
