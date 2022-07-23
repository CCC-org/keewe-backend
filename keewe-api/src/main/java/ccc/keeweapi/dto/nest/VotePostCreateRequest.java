package ccc.keeweapi.dto.nest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePostCreateRequest {
    private Long profileId;
    private List<String> candidates;
    private String contents;
}
