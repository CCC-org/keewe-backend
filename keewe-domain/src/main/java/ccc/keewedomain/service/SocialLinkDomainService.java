package ccc.keewedomain.service;

import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.SocialLink;
import ccc.keewedomain.repository.user.SocialLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialLinkDomainService {

    private final SocialLinkRepository socialLinkRepository;

    public SocialLink save(Profile profile, Link link) {
        SocialLink socialLink = SocialLink.of(profile, link);
        return socialLinkRepository.save(socialLink);
    }

    public List<SocialLink> saveAll(Profile profile, List<Link> links) {
        List<SocialLink> socialLinks = links.stream()
                .map(link -> SocialLink.of(profile, link))
                .collect(Collectors.toList());
        return socialLinkRepository.saveAll(socialLinks);
    }
}
