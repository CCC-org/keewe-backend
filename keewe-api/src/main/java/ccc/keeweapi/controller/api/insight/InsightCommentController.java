package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.request.CommentCreateRequest;
import ccc.keeweapi.dto.insight.response.*;
import ccc.keeweapi.service.insight.command.InsightCommentCommandApiService;
import ccc.keeweapi.service.insight.query.InsightCommentQueryApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class InsightCommentController {

    private final InsightCommentCommandApiService insightCommentCommandApiService;
    private final InsightCommentQueryApiService insightCommentQueryApiService;

    @PostMapping
    public ApiResponse<CommentCreateResponse> create(@RequestBody @Valid CommentCreateRequest request) {
        return ApiResponse.ok(insightCommentCommandApiService.create(request));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentDeleteResponse> delete(@PathVariable Long commentId) {
        return ApiResponse.ok(insightCommentCommandApiService.delete(commentId));
    }

    @GetMapping("/insights/{insightId}/preview")
    public ApiResponse<List<PreviewCommentResponse>> previewComments(@PathVariable Long insightId) {
        return ApiResponse.ok(insightCommentQueryApiService.getPreviewComments(insightId));
    }

    @GetMapping("/insights/{insightId}/count")
    public ApiResponse<InsightCommentCountResponse> countComment(@PathVariable Long insightId) {
        return ApiResponse.ok(insightCommentQueryApiService.getCommentCount(insightId));
    }

    @GetMapping("/insights/{insightId}")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable Long insightId,
            @RequestParam(required = false, defaultValue = "0") Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightCommentQueryApiService.getCommentsWithFirstReply(insightId, CursorPageable.of(cursor, limit)));
    }

    @GetMapping("{parentId}/replies")
    public ApiResponse<List<ReplyResponse>> getReplies(
            @PathVariable Long parentId,
            @RequestParam(required = false, defaultValue = "0") Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightCommentQueryApiService.getReplies(parentId, CursorPageable.of(cursor, limit)));
    }
}
