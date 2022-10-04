package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateDto {

    private Long writerId;
    private Long insightId;
    private Long parentId;
    private String content;

    public static CommentCreateDto of(Long writerId, Long insightId, Long parentId, String content) {
        return new CommentCreateDto(writerId, insightId, parentId, content);
    }
}
