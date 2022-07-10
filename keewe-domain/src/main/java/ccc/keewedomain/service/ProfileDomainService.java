package ccc.keewedomain.service;

import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDomainService {
    private final ProfileRepository profileRepository;

    public Long save(Profile profile) {
        return profileRepository.save(profile).getId();
    }
}
