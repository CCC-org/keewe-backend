package ccc.keeweapi.dto.nest;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.VotePostDto;
import org.springframework.stereotype.Component;

@Component
public class PostAssembler {

    public VotePostDto toVotePostDto(VotePostCreateRequest request) {
        return new VotePostDto(request.getCandidates(), request.getProfileId(), SecurityUtil.getUserId(), request.getContents());
    }

    public PostResponse toPostResponse(Long postId) {
        return new PostResponse(postId);
    }
}
