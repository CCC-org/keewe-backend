package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.AllAchievedTitleResponse;
import ccc.keeweapi.dto.user.MyPageTitleResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class UserTitleController {

    private final ProfileApiService profileApiService;

    @GetMapping("/achieved-title/{userId}")
    public ApiResponse<MyPageTitleResponse> getMyPageTitles(@PathVariable Long userId) {
        return ApiResponse.ok(profileApiService.getMyPageTitles(userId));
    }

    @GetMapping("/all-achieved-title/{userId}")
    public ApiResponse<AllAchievedTitleResponse> getAllAchievedTitles(@PathVariable Long userId) {
        return ApiResponse.ok(profileApiService.getAllAchievedTitles(userId));
    }
}
