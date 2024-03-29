package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.FollowUserListResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class UserRelationController {

    private final ProfileApiService profileApiService;

    @PostMapping("/follow/{targetId}")
    public ApiResponse<FollowToggleResponse> toggleFollowership(
            @PathVariable Long targetId,
            @RequestParam(required = false) Long insightId) {
        return ApiResponse.ok(profileApiService.toggleFollowership(targetId, insightId));
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
}
