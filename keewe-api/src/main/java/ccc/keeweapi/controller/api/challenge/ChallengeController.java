package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeQueryApiService;
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
    public ApiResponse<ChallengeCreateResponse> create(@RequestBody @Valid ChallengeCreateRequest request) {
        return ApiResponse.ok(challengeApiService.createChallenge(request));
    }

    @GetMapping("/specified-size")
    public ApiResponse<List<ChallengeInfoResponse>> getSpecifiedNumberOfChallenge(@RequestParam("size") @Min(1) @Max(10) Integer size) {
        return ApiResponse.ok(challengeApiService.getSpecifiedNumberOfChallenge(size));
    }

    @GetMapping("/history/specifed-size")
    public ApiResponse<ChallengeHistoryListResponse> getSpecifiedNumberOfChallengeHistory(
            @RequestParam(value = "size", defaultValue = KeeweConsts.LONG_MAX_STRING) @Min(1) Long size
    ) {
        return ApiResponse.ok(challengeApiService.getHistoryOfChallenge(size));
    }

    @GetMapping("/history") //TODO: path 일관성 확보 필요
    public ApiResponse<ChallengeHistoryListResponse> getHistoryOfChallenge(
            @RequestParam(value = "size", defaultValue = KeeweConsts.LONG_MAX_STRING) @Min(1) Long size
    ) {
        return ApiResponse.ok(challengeApiService.getHistoryOfChallenge(size));
    }

//    @GetMapping("/history")
//    public ApiResponse<?> paginateTerminatedChallenges() {
//
//    }

    @GetMapping
    public ApiResponse<List<OpenedChallengeResponse>> paginateChallenges(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(challengeQueryApiService.paginate(CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/detail/{challengeId}")
    public ApiResponse<ChallengeDetailResponse> getChallengeDetail(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getChallengeDetail(challengeId));
    }
}
