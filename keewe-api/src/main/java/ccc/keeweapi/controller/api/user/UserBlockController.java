package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.BlockUserResponse;
import ccc.keeweapi.dto.user.MyBlockUserListResponse;
import ccc.keeweapi.dto.user.UnblockUserResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class UserBlockController {

    private final ProfileApiService profileApiService;

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
}
