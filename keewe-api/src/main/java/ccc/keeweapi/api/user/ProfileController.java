package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.dto.ApiResponse;
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
    public ApiResponse<LinkCreateResponseDto> createLink(
            @RequestBody LinkCreateRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        LinkCreateResponseDto responseDto = profileService.createLink(requestDto, principal.getUser().getId());
        return ApiResponse.ok(responseDto);
    }

    @PostMapping("/nickname")
    public ApiResponse<NicknameCreateResponseDto> createNickname(
            @RequestBody NicknameCreateRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal) {

        NicknameCreateResponseDto responseDto = profileService.createNickname(
                requestDto.getProfileId(),
                principal.getUser().getId(),
                requestDto.getNickname());
        return ApiResponse.ok(responseDto);
    }

    @GetMapping("/activities")
    public ApiResponse<ActivitiesSearchResponseDto> searchActivities(@RequestParam("keyword") String keyword) {
        ActivitiesSearchResponseDto responseDto = profileService.searchActivities(keyword);
        return ApiResponse.ok(responseDto);
    }

    @PostMapping("/profile-links")
    public ApiResponse<?> createProfileLinks(@RequestBody ProfileLinkCreateRequestDto requestDto) {
        profileService.createProfileLinks(requestDto);
        return ApiResponse.ok();
    }
}
