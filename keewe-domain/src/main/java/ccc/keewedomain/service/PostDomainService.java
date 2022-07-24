package ccc.keewedomain.service;

import ccc.keewedomain.domain.nest.AnnouncementPost;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.dto.nest.AnnouncementCreateDto;
import ccc.keewedomain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDomainService {

    private final PostRepository postRepository;
    private final ProfileDomainService profileDomainService;

    public AnnouncementPost save(AnnouncementCreateDto dto) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(dto.getProfileId(), dto.getUserId());
        return (AnnouncementPost) postRepository.save(AnnouncementPost.of(profile, dto.getContent()));
    }
}
