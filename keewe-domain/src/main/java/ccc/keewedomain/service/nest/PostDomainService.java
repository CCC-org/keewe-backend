package ccc.keewedomain.service.nest;

import ccc.keewedomain.domain.nest.*;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.dto.nest.QuestionPostDto;
import ccc.keewedomain.dto.nest.VotePostDto;
import ccc.keewedomain.dto.nest.AnnouncementCreateDto;
import ccc.keewedomain.repository.nest.PostRepository;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostDomainService {
    private final ProfileDomainService profileDomainService;
    private final PostRepository postRepository;

    public Long createVotePost(VotePostDto votePostDto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(votePostDto.getProfileId(), votePostDto.getUserId());

        VotePost votePost = VotePost.from(profile, votePostDto.getContents());
        List<Candidate> candidates = createVoteCandidates(votePostDto.getCandidates(), votePost);
        votePost.createCandidates(candidates);

        return this.save(votePost);
    }

    public <T extends Post> Long save(T post) {
        return ((T) postRepository.save(post)).getId();
    }

    private List<Candidate> createVoteCandidates(List<String> candidates, VotePost votePost) {
        return candidates.stream()
                .map(it -> Candidate.from(it, votePost))
                .collect(Collectors.toList());
    }

    public Long createAnnouncementPost(AnnouncementCreateDto dto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getProfileId(), dto.getUserId());
        return this.save(AnnouncementPost.of(profile, dto.getContent()));
    }

    public Long createQuestionPost(QuestionPostDto dto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getProfileId(), dto.getUserId());
        return this.save(QuestionPost.of(profile, dto.getContent()));
    }

}
