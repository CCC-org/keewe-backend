package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.CommentCreateRequest;
import ccc.keeweapi.dto.insight.CommentCreateResponse;
import ccc.keeweapi.dto.insight.InsightCommentResponse;
import ccc.keeweapi.service.insight.CommentApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentApiService commentApiService;

    @PostMapping
    public ApiResponse<CommentCreateResponse> create(@RequestBody @Valid CommentCreateRequest request) {
        return ApiResponse.ok(commentApiService.create(request));
    }

    // 인사이트의 대표 댓글
    // 답글의 수가 가장 많은 댓글 1개 + 답글 최대 2개
    // 모든 댓글에 답글이 없는 경우 작성순 댓글 3개
    @GetMapping("/insight/{insightId}")
    public ApiResponse<InsightCommentResponse> getInsightComment(@PathVariable Long insightId) {
        return ApiResponse.ok();
    }
}
