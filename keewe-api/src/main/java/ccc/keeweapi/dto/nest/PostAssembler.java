package ccc.keeweapi.dto.nest;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.nest.AnnouncementPost;
import ccc.keewedomain.dto.nest.AnnouncementCreateDto;
import ccc.keewedomain.dto.VotePostDto;
import org.springframework.stereotype.Component;

@Component
public class PostAssembler {
    public AnnouncementCreateResponse toAnnouncementCreateResponse(AnnouncementPost post) {
        return AnnouncementCreateResponse.of(post.getId());
    }

    public AnnouncementCreateDto toAnnouncementCreateDto(AnnouncementCreateRequest request) {
        return AnnouncementCreateDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent());
    }

    public VotePostDto toVotePostDto(VotePostCreateRequest request) {
        return new VotePostDto(request.getCandidates(), request.getProfileId(), SecurityUtil.getUserId(), request.getContents());
    }

    public PostResponse toPostResponse(Long postId) {
        return new PostResponse(postId);
    }
}
