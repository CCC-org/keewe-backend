package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.service.challenge.command.ChallengeInviteApiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChallengeInviteController {
    private final ChallengeInviteApiService challengeInviteApiService;

    @PostMapping("/api/v1/challenge/invite")
    public ApiResponse<?> inviteChallenge(@RequestBody ChallengeInviteRequest request) {
        challengeInviteApiService.sendInvite(request.getTargetUserId(), request.getChallengeId());
        return ApiResponse.ok();
    }

    @Getter
    public static class ChallengeInviteRequest {
        private Long targetUserId;
        private Long challengeId;
    }
}
