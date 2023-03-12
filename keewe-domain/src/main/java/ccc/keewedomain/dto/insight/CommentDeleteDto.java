package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDeleteDto {

    private Long userId;
    private Long commentId;

    public static CommentDeleteDto of(Long userId, Long commentId) {
        return new CommentDeleteDto(userId, commentId);
    }
}