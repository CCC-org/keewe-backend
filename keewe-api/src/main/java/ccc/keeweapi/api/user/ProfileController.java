package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.service.user.ProfileService;
import ccc.keewedomain.domain.common.Link;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
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

        NicknameCreateResponseDto responseDto = profileService.createNickname(requestDto, principal.getUser().getId());
        return ApiResponse.ok(responseDto);
    }

    @PostMapping("/profile-links")
    public ApiResponse<?> createProfileLinks(@RequestBody ProfileLinkCreateRequestDto requestDto) {
        profileService.createProfileLinks(requestDto);
        return ApiResponse.ok();
    }
}
