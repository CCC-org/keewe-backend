package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.ActivitiesSearchResponse;
import ccc.keeweapi.dto.user.LinkCreateRequest;
import ccc.keewedomain.domain.common.enums.Activity;
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

import static ccc.keewedomain.domain.common.enums.Activity.인디;
import static ccc.keewedomain.domain.common.enums.Activity.축구;
import static ccc.keewedomain.domain.user.enums.ProfileStatus.LINK_NEEDED;
import static ccc.keewedomain.domain.user.enums.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        profileService.createLink(new LinkCreateRequest(profileId, "link_my._"), userId);
        Profile savedProfile = profileRepository.findById(profileId).get();
        assertThat(savedProfile.getId()).isEqualTo(profileId);
        System.out.println(savedProfile.getProfileStatus());
        assertThat(savedProfile.getProfileStatus().getOrder()).isEqualTo(LINK_NEEDED.getOrder() + 1);

        // 중복 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequest(profileId, "link_my._"), userId)
        );

        // 이상한 패턴 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequest(profileId, ".link"), userId)
        );
    }

    @Test
    @DisplayName("활동 분야 검색 테스트")
    void test2() {
        ActivitiesSearchResponse responseDto = profileService.searchActivities("인디");
        List<Activity> activities = responseDto.getActivities();
        assertTrue(activities.contains(인디));

        responseDto = profileService.searchActivities("축구");
        activities = responseDto.getActivities();
        assertTrue(activities.contains(축구));

        responseDto = profileService.searchActivities("야구");
        activities = responseDto.getActivities();
        assertFalse(activities.contains(축구)); // NLP 적용되면 검색 되는게 맞는거 같기도...?
    }
}