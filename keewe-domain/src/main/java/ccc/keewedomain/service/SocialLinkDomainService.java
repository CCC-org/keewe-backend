package ccc.keewedomain.service;

import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.SocialLink;
import ccc.keewedomain.repository.user.SocialLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLinkDomainService {

    private final SocialLinkRepository socialLinkRepository;

    public SocialLink save(Profile profile, Link link) {
        SocialLink socialLink = SocialLink.of(profile, link);
        return socialLinkRepository.save(socialLink);
    }
}
