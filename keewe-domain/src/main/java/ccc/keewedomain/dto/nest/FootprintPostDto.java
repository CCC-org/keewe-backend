package ccc.keewedomain.dto.nest;

import ccc.keewedomain.domain.nest.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class FootprintPostDto implements PostDto {
    private Long writerId;
    private Long profileId;
    private Long userId;
    private String content;
    private String postType;
    private Visibility visibility;
}
