package ccc.keeweapi.dto.nest;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.nest.enums.PostType;
import ccc.keewedomain.domain.nest.enums.Visibility;
import ccc.keewedomain.dto.nest.AnnouncementPostDto;
import ccc.keewedomain.dto.nest.FootprintPostDto;
import ccc.keewedomain.dto.nest.QuestionPostDto;
import ccc.keewedomain.dto.nest.VotePostDto;
import org.springframework.stereotype.Component;

import static ccc.keewecore.consts.KeeweRtnConsts.ERR506;

@Component
public class PostAssembler {
    public PostResponse toPostResponse(Long postId) {
        return PostResponse.of(postId);
    }

    public <T, S extends PostCreateRequest> T toAbstractPostDto(S request) {
        PostType postType = PostType.valueOf(request.getPostType());
        switch (postType) {
            case VOTE:
                return (T) toVotePostDto((VotePostCreateRequest) request);
            case ANNOUNCEMENT:
                return (T) toAnnouncementCreateDto(request);
            case QUESTION:
                return (T) toQuestionCreateDto(request);
            case FOOTPRINT:
                return (T) toFootprintCreateDto((FootprintPostCreateRequest) request);
            default:
                throw new KeeweException(ERR506);
        }
    }

    private AnnouncementPostDto toAnnouncementCreateDto(PostCreateRequest request) {
        return AnnouncementPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }

    private VotePostDto toVotePostDto(VotePostCreateRequest request) {
        return new VotePostDto(request.getCandidates(), request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }

    private QuestionPostDto toQuestionCreateDto(PostCreateRequest request) {
        return QuestionPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }

    private FootprintPostDto toFootprintCreateDto(FootprintPostCreateRequest request) {
        return FootprintPostDto.of(request.getWriterId(), request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType(), Visibility.valueOf(request.getVisibility()));
    }
}
