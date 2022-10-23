package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowCheckDto {
    private Long targetId;
    private Long userId;
}
