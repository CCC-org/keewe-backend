package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.user.LinkCreateRequestDto;
import ccc.keeweapi.dto.user.LinkCreateResponseDto;
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
    public ResponseEntity<LinkCreateResponseDto> createLink(
            @RequestBody LinkCreateRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        LinkCreateResponseDto responseDto = profileService.createLink(requestDto, principal.getUser().getId());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/nickname")
    public ResponseEntity<NicknameCreateResponseDto> createNickname(
            @RequestBody NicknameCreateRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal) {

        NicknameCreateResponseDto responseDto = profileService.createNickname(requestDto, principal.getUser().getId());
        return ResponseEntity.ok(responseDto);
    }
}
