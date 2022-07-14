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

    public Profile getByIdAndUserIdOrElseThrow(Long profileId, Long userId) {
        return profileRepository.findByIdAndUserIdAndDeletedFalse(profileId, userId).orElseThrow(() ->
                new IllegalArgumentException(String.format("profileId=[%d], userId=[%d]에 해당하는 프로필이 없습니다."))
        );
    }
}
