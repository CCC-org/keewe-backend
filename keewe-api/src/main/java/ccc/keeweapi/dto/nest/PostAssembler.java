package ccc.keeweapi.dto.nest;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.nest.AnnouncementPostDto;
import ccc.keewedomain.dto.nest.QuestionPostDto;
import ccc.keewedomain.dto.nest.VotePostDto;
import org.springframework.stereotype.Component;

import static ccc.keewecore.consts.KeeweRtnConsts.ERR506;

@Component
public class PostAssembler {
    public PostResponse toPostResponse(Long postId) {
        return PostResponse.of(postId);
    }

    public <T, S extends PostCreateRequest> T toAbstractPostDto(S request, String postType) {
        switch (postType) {
            case KeeweConsts.VOTE_POST:
                return (T) toVotePostDto((VotePostCreateRequest) request, postType);
            case KeeweConsts.ANNOUNCE_POST:
                return (T) toAnnouncementCreateDto(request, postType);
            case KeeweConsts.QUESTION_POST:
                return (T) toQuestionCreateDto(request, postType);
            default:
                throw new KeeweException(ERR506);
        }
    }

    private AnnouncementPostDto toAnnouncementCreateDto(PostCreateRequest request, String postType) {
        return AnnouncementPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), postType);
    }

    private VotePostDto toVotePostDto(VotePostCreateRequest request, String postType) {
        return new VotePostDto(request.getCandidates(), request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), postType);
    }

    private QuestionPostDto toQuestionCreateDto(PostCreateRequest request, String postType) {
        return QuestionPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), postType);
    }
}
