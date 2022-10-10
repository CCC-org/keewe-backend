package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowToggleDto {
    private Long userId;
    private Long targetId;
}
