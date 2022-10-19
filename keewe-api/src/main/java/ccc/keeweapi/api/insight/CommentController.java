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

    @GetMapping("/insight/{insightId}")
    public ApiResponse<InsightCommentResponse> getRepresentativeComments(@PathVariable Long insightId) {
        return ApiResponse.ok(commentApiService.getRepresentativeComments(insightId));
    }
}
