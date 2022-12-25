package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.service.insight.CommentApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static ccc.keewecore.consts.KeeweConsts.LONG_MAX_STRING;

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
            @RequestParam(required = false, defaultValue = LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(commentApiService.getCommentsWithFirstReply(insightId, CursorPageable.of(cursor, limit)));
    }

    @GetMapping("{parentId}/replies")
    public ApiResponse<List<ReplyResponse>> getReplies(
            @PathVariable Long parentId,
            @RequestParam(required = false, defaultValue = LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(commentApiService.getReplies(parentId, CursorPageable.of(cursor, limit)));
    }
}
