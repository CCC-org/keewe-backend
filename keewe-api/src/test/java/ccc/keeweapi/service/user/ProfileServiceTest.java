package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.LinkCreateRequestDto;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import ccc.keewedomain.repository.user.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static ccc.keewedomain.domain.user.enums.ProfileStatus.LINK_NEEDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class ProfileServiceTest {

    @Autowired
    ProfileService profileService;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    @DisplayName("링크 생성 테스트")
    void test1() {
        Long userId = 0L;
        Long profileId = 0L;
        // 정상적인 요청
        profileService.createLink(new LinkCreateRequestDto(profileId, "link_my._"), userId);
        Profile profile = profileRepository.findById(profileId).get();
        assertThat(profile.getId()).isEqualTo(profileId);
        assertThat(profile.getProfileStatus().getOrder()).isEqualTo(LINK_NEEDED.getOrder() + 1);

        // 중복 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequestDto(profileId, "link_my._"), userId)
        );

        // 이상한 패턴 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequestDto(profileId, ".link"), userId)
        );
    }
}