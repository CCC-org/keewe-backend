package ccc.keewedomain.dto.nest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePostDto implements PostDto {
    private List<String> candidates;
    private Long profileId;
    private Long userId;
    private String content;
    private String postType;
}
