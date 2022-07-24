package ccc.keeweapi.service.nest;

import ccc.keeweapi.dto.nest.*;
import ccc.keewedomain.domain.nest.AnnouncementPost;
import ccc.keewedomain.service.nest.PostDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostApiService {
    private final PostDomainService postDomainService;
    private final PostAssembler postAssembler;


    @Transactional
    public PostResponse createPost(VotePostCreateRequest request) {
        Long postId = postDomainService.createVotePost(postAssembler.toVotePostDto(request));
        return postAssembler.toPostResponse(postId);
    }

    @Transactional
    public AnnouncementCreateResponse createAnnouncementPost(AnnouncementCreateRequest request) {
        AnnouncementPost post = postDomainService.createAnnouncementPost(postAssembler.toAnnouncementCreateDto(request));
        return postAssembler.toAnnouncementCreateResponse(post);
    }
}
