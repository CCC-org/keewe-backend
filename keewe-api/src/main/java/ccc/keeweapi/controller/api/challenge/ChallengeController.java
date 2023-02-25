package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keewecore.consts.KeeweConsts;
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

    @PostMapping
    public ApiResponse<ChallengeCreateResponse> create(@RequestBody @Valid ChallengeCreateRequest request) {
        return ApiResponse.ok(challengeApiService.createChallenge(request));
    }

    @GetMapping("/specified-size")
    public ApiResponse<List<ChallengeInfoResponse>> getSpecifiedNumberOfChallenge(@RequestParam("size") @Min(1) @Max(10) Integer size) {
        return ApiResponse.ok(challengeApiService.getSpecifiedNumberOfChallenge(size));
    }

    @GetMapping("/detail/{challengeId}")
    public ApiResponse<ChallengeDetailResponse> getChallengeDetail(@PathVariable Long challengeId) {
        return ApiResponse.ok(challengeApiService.getChallengeDetail(challengeId));
    }

    @GetMapping("/history")
    public ApiResponse<ChallengeHistoryListResponse> getHistoryOfChallenge(
            @RequestParam(value = "size", defaultValue = KeeweConsts.LONG_MAX_STRING) @Min(1) Long size) {
        return ApiResponse.ok(challengeApiService.getHistoryOfChallenge(size));
    }
}
