package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.request.CommentCreateRequest;
import ccc.keeweapi.dto.insight.response.CommentCreateResponse;
import ccc.keeweapi.dto.insight.response.CommentDeleteResponse;
import ccc.keeweapi.dto.insight.response.CommentResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ReplyResponse;
import ccc.keeweapi.dto.insight.response.RepresentativeCommentsResponse;
import ccc.keeweapi.service.insight.command.InsightCommentCommandApiService;
import ccc.keeweapi.service.insight.query.InsightCommentQueryApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static ccc.keewecore.consts.KeeweConsts.LONG_MAX_STRING;

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

    @GetMapping("/representative/insights/{insightId}")
    public ApiResponse<RepresentativeCommentsResponse> getRepresentativeComments(@PathVariable Long insightId) {
        return ApiResponse.ok(RepresentativeCommentsResponse.dummy());
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
            @RequestParam(required = false, defaultValue = LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightCommentQueryApiService.getCommentsWithFirstReply(insightId, CursorPageable.of(cursor, limit)));
    }

    @GetMapping("{parentId}/replies")
    public ApiResponse<List<ReplyResponse>> getReplies(
            @PathVariable Long parentId,
            @RequestParam(required = false, defaultValue = LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightCommentQueryApiService.getReplies(parentId, CursorPageable.of(cursor, limit)));
    }
}
