package ccc.keewedomain.service.nest;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.nest.*;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.nest.AnnouncementPostDto;
import ccc.keewedomain.dto.nest.PostDto;
import ccc.keewedomain.dto.nest.QuestionPostDto;
import ccc.keewedomain.dto.nest.VotePostDto;
import ccc.keewedomain.repository.nest.PostRepository;
import ccc.keewedomain.service.user.UserDomainService;
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
    private final UserDomainService userDomainService;
    private final PostRepository postRepository;

    public <T extends PostDto> Long createPost(T dto) {
        switch (dto.getPostType()) {
            case KeeweConsts.VOTE_POST:
                return createVotePost((VotePostDto) dto);
            case KeeweConsts.ANNOUNCE_POST:
                return createAnnouncementPost((AnnouncementPostDto) dto);
            case KeeweConsts.QUESTION_POST:
                return createQuestionPost((QuestionPostDto) dto);
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
        User user = userDomainService.getUserByIdOrElseThrow(votePostDto.getUserId());
        VotePost votePost = VotePost.from(user, votePostDto.getContent());
        List<Candidate> candidates = createVoteCandidates(votePostDto.getCandidates(), votePost);
        votePost.createCandidates(candidates);

        return this.save(votePost);
    }

    private Long createAnnouncementPost(AnnouncementPostDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        return this.save(AnnouncementPost.of(user, dto.getContent()));
    }

    private Long createQuestionPost(QuestionPostDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        return this.save(QuestionPost.of(user, dto.getContent()));
    }

}
