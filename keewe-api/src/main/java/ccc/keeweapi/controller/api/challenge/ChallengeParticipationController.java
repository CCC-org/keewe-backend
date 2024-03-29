package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.ChallengeParticipateRequest;
import ccc.keeweapi.dto.challenge.ChallengeParticipationResponse;
import ccc.keeweapi.dto.challenge.ChallengeProgressResponse;
import ccc.keeweapi.dto.challenge.ChallengerCountResponse;
import ccc.keeweapi.dto.challenge.FinishedChallengeCountResponse;
import ccc.keeweapi.dto.challenge.FinishedChallengeResponse;
import ccc.keeweapi.dto.challenge.FriendResponse;
import ccc.keeweapi.dto.challenge.MyParticipationProgressResponse;
import ccc.keeweapi.dto.challenge.ParticipatingChallengeResponse;
import ccc.keeweapi.dto.challenge.ParticipationCheckResponse;
import ccc.keeweapi.dto.challenge.ParticipationUpdateRequest;
import ccc.keeweapi.dto.challenge.WeekProgressResponse;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.command.ChallengeParticipationCommandApiService;
import ccc.keeweapi.service.challenge.query.ChallengeParticipationQueryApiService;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
@Validated
public class ChallengeParticipationController {
    private final ChallengeApiService challengeApiService;
    private final ChallengeParticipationQueryApiService challengeParticipationQueryApiService;
    private final ChallengeParticipationCommandApiService challengeParticipationCommandApiService;

    @PostMapping(value = "/participation")
    public ApiResponse<ChallengeParticipationResponse> participate(@RequestBody @Valid ChallengeParticipateRequest request) {
        return ApiResponse.ok(challengeApiService.participate(request));
    }

    @GetMapping("/participating")
    public ApiResponse<ParticipatingChallengeResponse> getParticipatingChallenge() {
        return ApiResponse.ok(challengeApiService.getParticipatingChallenge());
    }

    @PatchMapping("/participating")
    public ApiResponse<Void> updateParticipation(@RequestBody ParticipationUpdateRequest request) {
        challengeApiService.updateParticipation(request);
        return ApiResponse.ok();
    }

    @GetMapping(value = "/participation/check")
    public ApiResponse<ParticipationCheckResponse> checkParticipation() {
        return ApiResponse.ok(challengeApiService.checkParticipation());
    }

    @GetMapping("/participation/progress/insight")
    public ApiResponse<MyParticipationProgressResponse> getMyParticipationProgress() {
        return ApiResponse.ok(challengeApiService.getMyParticipationProgress());
    }

    @GetMapping("/participation/my-week-progress")
    public ApiResponse<WeekProgressResponse> getMyThisWeekProgress() {
        return ApiResponse.ok(challengeApiService.getWeekProgress());
    }

    @GetMapping("/participation/progress")
    public ApiResponse<ChallengeProgressResponse> getChallengeProgress() {
        return ApiResponse.ok(challengeApiService.getProgress());
    }

    @GetMapping("/{challengeId}/friends")
    public ApiResponse<List<FriendResponse>> paginateFriends(
            @PathVariable Long challengeId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ApiResponse.ok(challengeApiService.paginateFriends(challengeId, pageable));
    }

    @GetMapping("/{challengeId}/challengers/count")
    public ApiResponse<ChallengerCountResponse> getChallengerCount(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getChallengerCount(challengeId));
    }

    @GetMapping("/finished")
    public ApiResponse<List<FinishedChallengeResponse>> paginateCompletedChallenges(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(challengeParticipationQueryApiService.paginateFinished(CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/finished/count")
    public ApiResponse<FinishedChallengeCountResponse> countCompletedChallenges() {
        return ApiResponse.ok(challengeParticipationQueryApiService.countFinished());
    }

    @DeleteMapping("/participating")
    public ApiResponse<Void> cancelParticipation() {
        challengeParticipationCommandApiService.deleteChallenge();
        return ApiResponse.ok();
    }
}
