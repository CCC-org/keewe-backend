package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.service.user.ProfileService;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/social-links")
    public ApiResponse<Void> createSocialLinks(@RequestBody SocialLinkCreateRequestDto requestDto) {
        User user = SecurityUtil.getUser();
        List<Link> links = requestDto.getLinks().stream()
                .map(linkDto -> Link.of(linkDto.getUrl(), linkDto.getType()))
                .collect(Collectors.toList());

        profileService.createSocialLinks(requestDto.getProfileId(), user.getId(), links);
        return ApiResponse.ok();
    }
}
