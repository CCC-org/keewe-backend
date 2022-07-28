package ccc.keewedomain.service.nest;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.nest.*;
import ccc.keewedomain.domain.nest.enums.PostType;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.dto.nest.*;
import ccc.keewedomain.repository.nest.PostRepository;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ccc.keewecore.consts.KeeweRtnConsts.ERR506;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostDomainService {
    private final ProfileDomainService profileDomainService;
    private final PostRepository postRepository;
    private final NestDomainService nestDomainService;

    public <T extends PostDto> Long createPost(T dto) {
        PostType postType = PostType.valueOf(dto.getPostType());
        switch (postType) {
            case VOTE:
                return createVotePost((VotePostDto) dto);
            case ANNOUNCEMENT:
                return createAnnouncementPost((AnnouncementPostDto) dto);
            case QUESTION:
                return createQuestionPost((QuestionPostDto) dto);
            case FOOTPRINT:
                return createFootprintPost((FootprintPostDto)dto);
            default:
                throw new KeeweException(ERR506);
        }
    }


    public <T extends Post> Long save(T post) {
        return ((T) postRepository.save(post)).getId();
    }

    private List<Candidate> createVoteCandidates(List<String> candidates, VotePost votePost) {
        return candidates.stream()
                .map(it -> Candidate.from(it, votePost))
                .collect(Collectors.toList());
    }

    private Long createVotePost(VotePostDto votePostDto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(votePostDto.getProfileId(), votePostDto.getUserId());

        VotePost votePost = VotePost.from(profile, votePostDto.getContent());
        List<Candidate> candidates = createVoteCandidates(votePostDto.getCandidates(), votePost);
        votePost.createCandidates(candidates);

        return this.save(votePost);
    }

    private Long createAnnouncementPost(AnnouncementPostDto dto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getProfileId(), dto.getUserId());
        return this.save(AnnouncementPost.of(profile, dto.getContent()));
    }

    private Long createQuestionPost(QuestionPostDto dto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getProfileId(), dto.getUserId());
        return this.save(QuestionPost.of(profile, dto.getContent()));
    }


    private Long createFootprintPost(FootprintPostDto dto) {
        Profile writer = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getWriterId(), dto.getUserId());
        Nest nest = nestDomainService.getByProfileIdOrElseThrow(dto.getProfileId());
        return this.save(FootprintPost.of(nest, writer, dto.getContent(), dto.getVisibility()));
    }
}
