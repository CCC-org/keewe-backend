package ccc.keeweapi.api.nest;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.nest.*;
import ccc.keeweapi.service.nest.PostApiService;
import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/nest")
@RequiredArgsConstructor
public class PostController {
    private final PostApiService postApiService;

    @PostMapping("/announcement")
    public ApiResponse<PostResponse> createAnnouncementPost(@Valid @RequestBody PostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request, KeeweConsts.ANNOUNCE_POST));
    }

    //TODO: 선택지 size, 선택지 내용 size, contents 145자 validate
    @PostMapping("/vote")
    public ApiResponse<PostResponse> createVotePost(@Valid @RequestBody VotePostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request, KeeweConsts.VOTE_POST));
    }

    @PostMapping("/question")
    public ApiResponse<PostResponse> createQuestionPost(@Valid @RequestBody PostCreateRequest request) {
        return ApiResponse.ok(postApiService.createPost(request, KeeweConsts.QUESTION_POST));
    }
}
