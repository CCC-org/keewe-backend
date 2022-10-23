package ccc.keewedomain.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class BookmarkToggleDto {
    private Long userId;
    private Long insightId;
}
