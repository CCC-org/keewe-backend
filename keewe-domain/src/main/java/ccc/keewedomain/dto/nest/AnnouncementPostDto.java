package ccc.keewedomain.dto.nest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class AnnouncementPostDto implements PostDto {
    private Long profileId;
    private Long userId;
    private String content;
    private String postType;
}
