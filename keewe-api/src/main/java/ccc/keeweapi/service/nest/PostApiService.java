package ccc.keeweapi.service.nest;

import ccc.keeweapi.component.PostAssembler;
import ccc.keeweapi.dto.nest.*;
import ccc.keewecore.aop.annotations.FLogging;
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
    @FLogging
    public <T extends PostCreateRequest> PostResponse createPost(T request, String postType) {
        Long postId = postDomainService.createPost(postAssembler.toAbstractPostDto(request, postType));
        return postAssembler.toPostResponse(postId);
    }
}
