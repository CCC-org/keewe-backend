package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileApiService profileApiService;

    @PostMapping
    public ApiResponse<OnboardResponse> onboard(@RequestBody @Valid OnboardRequest request) {
        return ApiResponse.ok(profileApiService.onboard(request));
    }

    @PostMapping("/follow/{targetId}")
    public ApiResponse<FollowToggleResponse> toggleFollowership(@PathVariable Long targetId) {
        return ApiResponse.ok(profileApiService.toggleFollowership(targetId));
    }

    @PostMapping("/photo")
    public ApiResponse<UploadProfilePhotoResponse> uploadProfilePhoto(@RequestParam("image") MultipartFile imageFile) {
        return ApiResponse.ok(profileApiService.uploadProfilePhoto(imageFile));
    }

    @GetMapping("/{targetId}")
    public ApiResponse<ProfileMyPageResponse> getMyPageProfile(@PathVariable Long targetId) {
        return ApiResponse.ok(profileApiService.getMyPageProfile(targetId));
    }

    @GetMapping("/achieved-title/{userId}")
    public ApiResponse<MyPageTitleResponse> getMyPageTitles(@PathVariable Long userId) {
        return ApiResponse.ok(profileApiService.getMyPageTitles(userId));
    }

    @GetMapping("/all-achieved-title/{userId}")
    public ApiResponse<List<AchievedTitleResponse>> getAllAchievedTitles(@PathVariable Long userId) {
        return ApiResponse.ok(profileApiService.getAllAchievedTitles(userId));
    }
}
