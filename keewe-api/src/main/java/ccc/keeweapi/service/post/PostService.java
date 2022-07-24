package ccc.keeweapi.service.post;

import ccc.keeweapi.dto.nest.AnnouncementCreateRequest;
import ccc.keeweapi.dto.nest.AnnouncementCreateResponse;
import ccc.keeweapi.dto.nest.PostAssembler;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.nest.AnnouncementPost;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.service.PostDomainService;
import ccc.keewedomain.service.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDomainService postDomainService;
    private final PostAssembler postAssembler;

    @Transactional
    public AnnouncementCreateResponse createAnnouncementPost(AnnouncementCreateRequest request) {
        AnnouncementPost post = postDomainService.save(postAssembler.toAnnouncementCreateDto(request));
        return postAssembler.toAnnouncementCreateResponse(post);
    }
}
