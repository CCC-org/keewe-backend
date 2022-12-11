package ccc.keeweapi.service.user;

import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.dto.user.ProfileMyPageResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileApiService {

    private final ProfileDomainService profileDomainService;
    private final UserDomainService userDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final ProfileAssembler profileAssembler;

    @Transactional
    public OnboardResponse onboard(OnboardRequest request) {
        User user = profileDomainService.onboard(profileAssembler.toOnboardDto(request));
        return profileAssembler.toOnboardResponse(user);
    }

    @Transactional
    public FollowToggleResponse toggleFollowership(Long targetId) {
        boolean following = profileDomainService.toggleFollowership(profileAssembler.toFollowToggleDto(targetId));
        return profileAssembler.toFollowToggleResponse(following);
    }

    @Transactional(readOnly = true)
    public ProfileMyPageResponse getMyPageProfile(Long targetId) {
        User targetUser = userDomainService.getUserByIdWithInterests(targetId);
        Long userId = SecurityUtil.getUserId();

        boolean isFollowing = profileDomainService.isFollowing(FollowCheckDto.of(targetId, userId));
        Long followerCount = profileDomainService.getFollowerCount(targetUser);
        Long followingCount = profileDomainService.getFollowingCount(targetUser);
        String challengeName = challengeDomainService.findCurrentParticipationWithChallenge(targetId)
                .map(challengeParticipation -> challengeParticipation.getChallenge().getName())
                .orElse(null);

        return profileAssembler.toProfileMyPageResponse(targetUser, isFollowing, followerCount, followingCount, challengeName);
    }
}
