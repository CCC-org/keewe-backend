package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.ActivitiesSearchResponse;
import ccc.keeweapi.dto.user.LinkCreateRequest;
import ccc.keeweapi.security.WithKeeweUser;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.repository.user.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ccc.keewedomain.domain.common.enums.Activity.*;
import static ccc.keewedomain.domain.user.enums.ProfileStatus.LINK_NEEDED;
import static ccc.keewedomain.domain.user.enums.ProfileStatus.SOCIAL_LINK_NEEDED;
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
    @WithKeeweUser(email = "yk@keewe.com")
    void test1() {
        // given
        User user = SecurityUtil.getUser();
        userRepository.save(user);
        System.out.println("user = " + user.getId());
        Profile profile = Profile.init().user(user).build();
        profileRepository.save(profile);

        Long profileId = profile.getId();

        // 정상적인 요청
        profileService.createLink(new LinkCreateRequest(profileId, "link_my._"));
        Profile savedProfile = profileRepository.findById(profileId).get();
        assertThat(savedProfile.getId()).isEqualTo(profileId);
        System.out.println(savedProfile.getProfileStatus());
        assertThat(savedProfile.getProfileStatus()).isEqualTo(SOCIAL_LINK_NEEDED);

        // 중복 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequest(profileId, "link_my._"))
        );

        // 이상한 패턴 요청
        assertThrows(IllegalArgumentException.class, () ->
                profileService.createLink(new LinkCreateRequest(profileId, ".link"))
        );
    }

    @Test
    @DisplayName("활동 분야 검색 테스트")
    void test2() {
        ActivitiesSearchResponse responseDto = profileService.searchActivities("인디");
        List<Activity> activities = responseDto.getActivities();
        assertTrue(activities.contains(INDIE));

        responseDto = profileService.searchActivities("음악");
        activities = responseDto.getActivities();
        assertTrue(activities.contains(OTHER_MUSIC));

        responseDto = profileService.searchActivities("스케이트보드");
        activities = responseDto.getActivities();
        assertTrue(activities.contains(BOARD));

        responseDto = profileService.searchActivities("불어");
        activities = responseDto.getActivities();
        assertFalse(activities.contains(OTHER_LANGUAGE)); // NLP 적용되면 바뀌어야 함...

    }
}