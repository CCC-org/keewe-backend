package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.CreateLinkDto;
import ccc.keeweapi.dto.user.NicknameCreateRequestDto;
import ccc.keeweapi.dto.user.NicknameCreateResponseDto;
import ccc.keeweapi.service.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/link")
    public Long createLink() {
        return profileService.createLink(new CreateLinkDto(4L, "hs"));
    }

    @PostMapping("/nickname")
    public ApiResponse<NicknameCreateResponseDto> createNickname(
            @RequestBody NicknameCreateRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal) {

        NicknameCreateResponseDto responseDto = profileService.createNickname(requestDto, principal.getUser().getId());
        return ApiResponse.ok(responseDto);
    }
}
