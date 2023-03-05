package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import ccc.keeweapi.service.challenge.query.ChallengeParticipationQueryApiService;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
@Validated
public class ChallengeParticipationController {
    private final ChallengeApiService challengeApiService;
    private final ChallengeParticipationQueryApiService challengeParticipationQueryApiService;

    @PostMapping(value = "/participation")
    public ApiResponse<ChallengeParticipationResponse> participate(@RequestBody @Valid ChallengeParticipateRequest request) {
        return ApiResponse.ok(challengeApiService.participate(request));
    }

    @GetMapping("/participating")
    public ApiResponse<ParticipatingChallengeResponse> getParticipatingChallenge() {
        return ApiResponse.ok(challengeApiService.getParticipatingChallenege());
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

    @GetMapping("/{challengeId}/challengers")
    public ApiResponse<List<TogetherChallengerResponse>> getTogetherChallengers(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getTogetherChallengers(challengeId));
    }

    @GetMapping("/{challengeId}/challengers/count")
    public ApiResponse<ChallengerCountResponse> getChallengerCount(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getChallengerCount(challengeId));
    }

    @GetMapping("/history")
    @FLogging
    public ApiResponse<ChallengeHistoryListResponse> getHistoryOfChallenge(
            @RequestParam(value = "size", defaultValue = KeeweConsts.LONG_MAX_STRING) @Min(1) Long size
    ) {
        return ApiResponse.ok(challengeApiService.getHistoryOfChallenge(size));
    }

    @GetMapping("/completed")
    public ApiResponse<?> paginateCompletedChallenges(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(challengeParticipationQueryApiService.paginateCompleted(CursorPageable.of(cursor, limit)));
    }
}
