package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.service.insight.InsightApiService;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class InsightController {

    private final InsightApiService insightApiService;

    @PostMapping
    public ApiResponse<InsightCreateResponse> create(@RequestBody @Valid InsightCreateRequest request) {
        return ApiResponse.ok(insightApiService.create(request));
    }

    @PostMapping("/view/{insightId}")
    public ApiResponse incrementViewCount(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.incrementViewCount(insightId));
    }

    @GetMapping("/{insightId}")
    public ApiResponse<InsightGetResponse> getInsight(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getInsight(insightId));
    }

    @GetMapping
    public ApiResponse<List<InsightGetForHomeResponse>> getInsightsForHome(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit,
            @RequestParam(required = false, defaultValue = "false") Boolean follow
    ) {
        return ApiResponse.ok(insightApiService.getInsightsForHome(CursorPageable.of(cursor, limit), follow));
    }

    @GetMapping("/author/{insightId}")
    public ApiResponse<InsightAuthorAreaResponse> getInsightAuthorAreaInfo(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getInsightAuthorAreaInfo(insightId));
    }

    @PostMapping("/bookmark/{insightId}")
    @FLogging
    public ApiResponse<BookmarkToggleResponse> toggleBookmark(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.toggleInsightBookmark(insightId));
    }

    @GetMapping("/{insightId}/challenge-record")
    public ApiResponse<ChallengeRecordResponse> getChallengeRecord(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getChallengeRecord(insightId));
    }

    @GetMapping("/my-page/{userId}")
    public ApiResponse<List<InsightMyPageResponse>> getInsightForMyPage(
            @PathVariable Long userId,
            @RequestParam(required = false) Long drawerId,
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightApiService.getInsightsForMyPage(userId, drawerId, CursorPageable.of(cursor, limit)));
    }
}
