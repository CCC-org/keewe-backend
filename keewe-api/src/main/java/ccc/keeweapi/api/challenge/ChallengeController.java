package ccc.keeweapi.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeApiService challengeApiService;

    @PostMapping
    public ApiResponse<ChallengeCreateResponse> create(@RequestBody @Valid ChallengeCreateRequest request) {
        return ApiResponse.ok(challengeApiService.createChallenge(request));
    }

    @PostMapping(value = "/participation")
    public ApiResponse<ChallengeParticipationResponse> participate(@RequestBody @Valid ChallengeParticipateRequest request) {
        return ApiResponse.ok(challengeApiService.participate(request));
    }

    @GetMapping(value = "/participation/check")
    public ApiResponse<ParticipationCheckResponse> checkParticipation() {
        return ApiResponse.ok(challengeApiService.checkParticipation());
    }

    @GetMapping("/participation/progress/insight")
    public ApiResponse<InsightProgressResponse> getMyParticipationProgress() {
        return ApiResponse.ok(challengeApiService.getMyParticipationProgress());
    }

    @GetMapping("/participation/this-week")
    public ApiResponse<WeekProgressResponse> getMyThisWeekProgress() {
        return ApiResponse.ok(challengeApiService.getWeekProgress());
    }
}
