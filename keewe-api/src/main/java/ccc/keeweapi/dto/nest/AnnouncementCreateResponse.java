package ccc.keeweapi.dto.nest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class AnnouncementCreateResponse {
    private Long postId;
}
