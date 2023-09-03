package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.ChallengeCreateRequest;
import ccc.keeweapi.dto.challenge.ChallengeCreateResponse;
import ccc.keeweapi.dto.challenge.ChallengeDetailResponse;
import ccc.keeweapi.dto.challenge.ChallengeInsightNumberResponse;
import ccc.keeweapi.dto.challenge.ChallengeStatisticsResponse;
import ccc.keeweapi.dto.challenge.OpenedChallengeResponse;
import ccc.keeweapi.dto.challenge.ParticipatingChallengeDetailResponse;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeQueryApiService;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{challengeId}/statistics")
    @FLogging
    public ApiResponse<ChallengeStatisticsResponse> aggregateChallengeStatisticsV2(@PathVariable Long challengeId) {
        return ApiResponse.ok(ChallengeStatisticsResponse.of(3L, 4L, 5L, 6L, 7L));
    }

    @GetMapping("/my/insight/count")
    public ApiResponse<ChallengeInsightNumberResponse> countInsightOfChallenge(
            @RequestParam(required = false) Long writerId
    ) {
        return ApiResponse.ok(challengeApiService.countInsightOfChallenge(writerId));
    }
}
