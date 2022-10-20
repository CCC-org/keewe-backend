package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.CommentCreateRequest;
import ccc.keeweapi.dto.insight.CommentCreateResponse;
import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.RepresentativeCommentResponse;
import ccc.keeweapi.service.insight.CommentApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentApiService commentApiService;

    @PostMapping
    public ApiResponse<CommentCreateResponse> create(@RequestBody @Valid CommentCreateRequest request) {
        return ApiResponse.ok(commentApiService.create(request));
    }

    @GetMapping("/representative/insights/{insightId}")
    public ApiResponse<RepresentativeCommentResponse> getRepresentativeComments(@PathVariable Long insightId) {
        return ApiResponse.ok(commentApiService.getRepresentativeComments(insightId));
    }

    @GetMapping("/insights/{insightId}")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable Long insightId,
            @RequestParam Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(commentApiService.getCommentsWithFirstReply(insightId, CursorPageable.of(cursor, limit)));
    }
}
