package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// FIXME 제거
@Getter
@AllArgsConstructor(staticName = "of")
public class RepresentativeCommentsResponse {
    private Long total;
    private List<ActiveCommentResponse> comments;

    public static RepresentativeCommentsResponse dummy() {
        return RepresentativeCommentsResponse.of(0L, List.of());
    }
}
