package ccc.keeweapi.api.nest;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.nest.AnnouncementCreateRequest;
import ccc.keeweapi.dto.nest.AnnouncementCreateResponse;
import ccc.keeweapi.service.post.PostService;
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

    private final PostService postService;

    @PostMapping("/announcement")
    public ApiResponse<AnnouncementCreateResponse> createAnnouncement(@Valid @RequestBody AnnouncementCreateRequest request) {
        return ApiResponse.ok(postService.createAnnouncementPost(request));
    }
}
