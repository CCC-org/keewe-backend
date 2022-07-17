package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/link")
    public ApiResponse<LinkCreateResponse> createLink(
            @RequestBody LinkCreateRequest requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        LinkCreateResponse responseDto = profileService.createLink(requestDto, principal.getUser().getId());
        return ApiResponse.ok(responseDto);
    }

    @PostMapping("/nickname")
    public ApiResponse<NicknameCreateResponse> createNickname(@RequestBody NicknameCreateRequest requestDto) {
        return ApiResponse.ok(profileService.createNickname(requestDto));
    }

    @GetMapping("/activities")
    public ApiResponse<ActivitiesSearchResponse> searchActivities(@RequestParam("keyword") String keyword) {
        return ApiResponse.ok(profileService.searchActivities(keyword));
    }

    @PostMapping("/social-links")
    public ApiResponse<Void> createSocialLinks(@RequestBody SocialLinkCreateRequest request) {
        profileService.createSocialLinks(request);
        return ApiResponse.ok();
    }

    @GetMapping("/incomplete")
    public ApiResponse<IncompleteProfileResponse> getIncompleteProfiles() {
        return ApiResponse.ok(profileService.getIncompleteProfile());
    }
}
