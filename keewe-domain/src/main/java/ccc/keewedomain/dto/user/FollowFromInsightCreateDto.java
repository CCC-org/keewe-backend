package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowFromInsightCreateDto {
    private Long followerId;
    private Long followeeId;
    private Long insightId;
}
