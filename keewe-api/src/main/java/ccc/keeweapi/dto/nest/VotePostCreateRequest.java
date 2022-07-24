package ccc.keeweapi.dto.nest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePostCreateRequest extends PostCreateRequest {
    @Size(min = 2, max = 5)
    private List<String> candidates;
}
