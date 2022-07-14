package ccc.keewedomain.service;

import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.ProfileLink;
import ccc.keewedomain.repository.user.ProfileLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileLinkDomainService {

    private final ProfileLinkRepository profileLinkRepository;

    public ProfileLink save(Profile profile, Link link) {
        ProfileLink profileLink = ProfileLink.of(profile, link);
        return profileLinkRepository.save(profileLink);
    }
}
