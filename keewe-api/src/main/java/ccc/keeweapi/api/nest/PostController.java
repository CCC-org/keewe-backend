package ccc.keeweapi.api.nest;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.nest.*;
import ccc.keeweapi.service.nest.PostApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/nest")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostApiService postApiService;

    @PostMapping("/announcement")
    public ApiResponse<PostResponse> createAnnouncementPost(@Valid @RequestBody CommonPostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request));
    }

    //TODO: 선택지 size, 선택지 내용 size, contents 145자 validate
    @PostMapping("/vote")
    public ApiResponse<PostResponse> createVotePost(@Valid @RequestBody VotePostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request));
    }

    @PostMapping("/question")
    public ApiResponse<PostResponse> createQuestionPost(@Valid @RequestBody CommonPostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request));
    }

    @PostMapping("/sample")
    public <T extends AbstractPostCreateRequest> ApiResponse<?> createSamplePost(@RequestBody T request) {
        log.info("request {}", request.toString());
        return ApiResponse.ok(postApiService.createPost(request));
    }
}
