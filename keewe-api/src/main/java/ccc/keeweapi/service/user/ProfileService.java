package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.CreateLinkDto;

import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ccc.keewedomain.domain.user.enums.ProfileStatus.ACTIVITIES_NEEDED;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Long createLink(CreateLinkDto createLinkDto) {
        String link = createLinkDto.getLink();
        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(createLinkDto.getProfileId(), 5L);

        checkDuplicateLinkOrElseThrows(link);

        Profile entity = profileRepository.save(profile.mutate()
                .link(link)
                .profileStatus(ACTIVITIES_NEEDED)
                .build());

        return 0L;
    }
    
    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("허보성 바보!!");
    }
}