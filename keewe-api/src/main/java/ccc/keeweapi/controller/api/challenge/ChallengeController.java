package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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

    @GetMapping("/participation/my-week-progress")
    public ApiResponse<WeekProgressResponse> getMyThisWeekProgress() {
        return ApiResponse.ok(challengeApiService.getWeekProgress());
    }

    @GetMapping("/participating")
    public ApiResponse<ParticipatingChallengeResponse> getParticipatingChallenge() {
        return ApiResponse.ok(challengeApiService.getParticipatingChallenege());
    }

    @GetMapping("/specified-size")
    public ApiResponse<List<ChallengeInfoResponse>> getSpecifiedNumberOfChallenge(@RequestParam("size") @Min(1) @Max(10) Integer size) {
        return ApiResponse.ok(challengeApiService.getSpecifiedNumberOfChallenge(size));
    }

    @GetMapping("/history")
    public ApiResponse<ChallengeHistoryListResponse> getHistoryOfChallenge(
            @RequestParam(value = "size", defaultValue = KeeweConsts.LONG_MAX_STRING) @Min(1) Long size) {
        return ApiResponse.ok(challengeApiService.getHistoryOfChallenge(size));
    }
}
