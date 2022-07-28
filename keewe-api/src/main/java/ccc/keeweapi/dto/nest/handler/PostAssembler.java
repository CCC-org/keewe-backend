package ccc.keeweapi.dto.nest.handler;

import ccc.keeweapi.dto.nest.AbstractPostCreateRequest;
import ccc.keeweapi.dto.nest.CommonPostCreateRequest;
import ccc.keeweapi.dto.nest.PostResponse;
import ccc.keeweapi.dto.nest.VotePostCreateRequest;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.nest.AnnouncementPostDto;
import ccc.keewedomain.dto.nest.PostDto;
import ccc.keewedomain.dto.nest.QuestionPostDto;
import ccc.keewedomain.dto.nest.VotePostDto;
import org.springframework.stereotype.Component;

import static ccc.keewecore.consts.KeeweRtnConsts.ERR506;

@Component
public class PostAssembler {
    public PostResponse toPostResponse(Long postId) {
        return PostResponse.of(postId);
    }

    public <T extends PostDto, S extends AbstractPostCreateRequest> T toAbstractPostDto(S request) {
        switch (request.getPostType()) {
            case KeeweConsts.VOTE_POST:
                return toVotePostDto((VotePostCreateRequest) request);
            case KeeweConsts.ANNOUNCE_POST:
                return toAnnouncementCreateDto((CommonPostCreateRequest) request);
            case KeeweConsts.QUESTION_POST:
                return toQuestionCreateDto((CommonPostCreateRequest) request);
            default:
                throw new KeeweException(ERR506);
        }
    }

    private <T extends PostDto> T toAnnouncementCreateDto(CommonPostCreateRequest request) {
        return (T) AnnouncementPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }

    private <T extends PostDto> T toVotePostDto(VotePostCreateRequest request) {
        return (T) VotePostDto.of(request.getCandidates(), request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }

    private <T extends PostDto> T toQuestionCreateDto(CommonPostCreateRequest request) {
        return (T) QuestionPostDto.of(request.getProfileId(), SecurityUtil.getUserId(), request.getContent(), request.getPostType());
    }
}
