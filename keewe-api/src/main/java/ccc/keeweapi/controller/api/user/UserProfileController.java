package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewecore.aop.annotations.FLogging;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final ProfileApiService profileApiService;

    @PostMapping
    public ApiResponse<OnboardResponse> onboard(@RequestBody @Valid OnboardRequest request) {
        return ApiResponse.ok(profileApiService.onboard(request));
    }

    @PostMapping("/photo")
    public ApiResponse<UploadProfilePhotoResponse> uploadProfilePhoto(@RequestParam("image") MultipartFile imageFile) {
        return ApiResponse.ok(profileApiService.uploadProfilePhoto(imageFile));
    }

    // TODO. targetId 네이밍 변경
    @GetMapping("/{targetId}")
    public ApiResponse<ProfileMyPageResponse> getMyPageProfile(@PathVariable Long targetId) {
        return ApiResponse.ok(profileApiService.getMyPageProfile(targetId));
    }

    @PatchMapping
    @FLogging
    public ApiResponse<ProfileUpdateResponse> updateProfile(@Valid @ModelAttribute ProfileUpdateRequest request) {
        return ApiResponse.ok(profileApiService.updateProfile(request));
    }
    
    @GetMapping("/interests")
    public ApiResponse<InterestsResponse> getInterests() {
        return ApiResponse.ok(profileApiService.getInterests());
    }
}
