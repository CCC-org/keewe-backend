package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileApiService {

    private final ProfileDomainService profileDomainService;
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
}
