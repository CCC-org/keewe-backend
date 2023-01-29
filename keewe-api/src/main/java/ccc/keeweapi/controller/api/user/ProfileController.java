package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public ApiResponse<AllAchievedTitleResponse> getAllAchievedTitles(@PathVariable Long userId) {
        return ApiResponse.ok(profileApiService.getAllAchievedTitles(userId));
    }

    @GetMapping("/follower/{userId}")
    public ApiResponse<FollowUserListResponse> getFollowers(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
            @RequestParam Long limit) {
        CursorPageable<LocalDateTime> cPage = CursorPageable.of(cursor, limit);
        return ApiResponse.ok(profileApiService.getFollowers(userId, cPage));
    }

    @GetMapping("/followee/{userId}")
    public ApiResponse<FollowUserListResponse> getFollowees(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
            @RequestParam Long limit) {
        CursorPageable<LocalDateTime> cPage = CursorPageable.of(cursor, limit);
        return ApiResponse.ok(profileApiService.getFollowees(userId, cPage));
    }

    @PostMapping("/block/{blockedUserId}")
    public ApiResponse<BlockUserResponse> blockUser(@PathVariable Long blockedUserId) {
        return ApiResponse.ok(profileApiService.blockUser(blockedUserId));
    }

    @DeleteMapping("/block/{blockedUserId}")
    public ApiResponse<UnblockUserResponse> unblockUser(@PathVariable Long blockedUserId) {
        return ApiResponse.ok(profileApiService.unblockUser(blockedUserId));
    }

    @GetMapping("/my-block-list")
    public ApiResponse<MyBlockUserListResponse> getMyBlockList() {
        return ApiResponse.ok(profileApiService.getMyBlockList());
    }

    @PatchMapping
    public ApiResponse<ProfileUpdateResponse> updateProfile(@Valid @ModelAttribute ProfileUpdateRequest request) {
        return ApiResponse.ok(profileApiService.updateProfile(request));
    }
}
