package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.LinkCreateRequestDto;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ccc.keewedomain.domain.user.enums.ProfileStatus.LINK_NEEDED;
import static ccc.keewedomain.domain.user.enums.UserStatus.ACTIVE;
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
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("링크 생성 테스트")
    void test1() {
        // given
        User user = userRepository.save(new User(0L, "yk@keewe.com", "password", "000", List.of(), ACTIVE, false));
        Profile profile = Profile.init().user(user).build();
        profileRepository.save(profile);
        Long profileId = profile.getId();
        Long userId = user.getId();
        // 정상적인 요청
        profileService.createLink(new LinkCreateRequestDto(profileId, "link_my._"), userId);
        Profile savedProfile = profileRepository.findById(profileId).get();
        assertThat(savedProfile.getId()).isEqualTo(profileId);
        System.out.println(savedProfile.getProfileStatus());
        assertThat(savedProfile.getProfileStatus().getOrder()).isEqualTo(LINK_NEEDED.getOrder() + 1);

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