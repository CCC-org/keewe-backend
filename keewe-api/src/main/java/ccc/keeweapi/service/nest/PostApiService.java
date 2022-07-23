package ccc.keeweapi.service.nest;

import ccc.keeweapi.dto.nest.PostAssembler;
import ccc.keeweapi.dto.nest.PostResponse;
import ccc.keeweapi.dto.nest.VotePostCreateRequest;
import ccc.keewedomain.service.PostDomainService;
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

}
