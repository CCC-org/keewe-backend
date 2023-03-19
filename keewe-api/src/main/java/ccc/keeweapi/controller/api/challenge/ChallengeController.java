package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeQueryApiService;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
@Validated
public class ChallengeController {

    private final ChallengeApiService challengeApiService;
    private final ChallengeQueryApiService challengeQueryApiService;

    @PostMapping
    @FLogging
    public ApiResponse<ChallengeCreateResponse> create(@RequestBody @Valid ChallengeCreateRequest request) {
        return ApiResponse.ok(challengeApiService.createChallenge(request));
    }

    @GetMapping
    @FLogging
    public ApiResponse<List<OpenedChallengeResponse>> paginateChallenges(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(challengeQueryApiService.paginate(CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/specified-size")
    @Deprecated
    @FLogging
    public ApiResponse<List<ChallengeInfoResponse>> getSpecifiedNumberOfChallenge(@RequestParam("size") @Min(1) @Max(10) Integer size) {
        return ApiResponse.ok(challengeApiService.getSpecifiedNumberOfChallenge(size));
    }

    @GetMapping("/{challengeId}/detail")
    @FLogging
    public ApiResponse<ChallengeDetailResponse> getChallengeDetail(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getChallengeDetail(challengeId));
    }

    @GetMapping("/my/detail")
    @FLogging
    public ApiResponse<ParticipatingChallengeDetailResponse> getMyChallengeDetail() {
        return ApiResponse.ok(challengeApiService.getMyChallengeDetail());
    }

    @GetMapping("/statistics")
    @FLogging
    public ApiResponse<ChallengeStatisticsResponse> aggregateChallengeStatistics() {
        return ApiResponse.ok(challengeQueryApiService.aggregateChallengeStatistics());
    }
}
