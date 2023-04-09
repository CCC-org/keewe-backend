package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.ChallengeRecordResponse;
import ccc.keeweapi.dto.insight.InsightAuthorAreaResponse;
import ccc.keeweapi.dto.insight.InsightCreateRequest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keeweapi.dto.insight.InsightDeleteResponse;
import ccc.keeweapi.dto.insight.InsightGetForHomeResponse;
import ccc.keeweapi.dto.insight.InsightGetResponse;
import ccc.keeweapi.dto.insight.InsightMyPageResponse;
import ccc.keeweapi.service.insight.command.InsightCommandApiService;
import ccc.keeweapi.service.insight.query.InsightQueryApiService;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class InsightController {

    private final InsightQueryApiService insightQueryApiService;
    private final InsightCommandApiService insightCommandApiService;

    @PostMapping
    public ApiResponse<InsightCreateResponse> create(@RequestBody @Valid InsightCreateRequest request) {
        return ApiResponse.ok(insightCommandApiService.create(request));
    }

    @PostMapping("/view/{insightId}")
    public ApiResponse incrementViewCount(@PathVariable Long insightId) {
        return ApiResponse.ok(insightCommandApiService.incrementViewCount(insightId));
    }

    @GetMapping("/{insightId}")
    public ApiResponse<InsightGetResponse> getInsight(@PathVariable Long insightId) {
        return ApiResponse.ok(insightQueryApiService.getInsight(insightId));
    }

    @DeleteMapping("/{insightId}")
    public ApiResponse<InsightDeleteResponse> deleteInsight(@PathVariable Long insightId) {
        return ApiResponse.ok(insightCommandApiService.delete(insightId));
    }

    @GetMapping
    public ApiResponse<List<InsightGetForHomeResponse>> getInsightsForHome(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit,
            @RequestParam(required = false, defaultValue = "false") Boolean follow
    ) {
        return ApiResponse.ok(insightQueryApiService.getInsightsForHome(CursorPageable.of(cursor, limit), follow));
    }

    @GetMapping("/author/{insightId}")
    public ApiResponse<InsightAuthorAreaResponse> getInsightAuthorAreaInfo(@PathVariable Long insightId) {
        return ApiResponse.ok(insightQueryApiService.getInsightAuthorAreaInfo(insightId));
    }

    @GetMapping("/{insightId}/challenge-record")
    public ApiResponse<ChallengeRecordResponse> getChallengeRecord(@PathVariable Long insightId) {
        return ApiResponse.ok(insightQueryApiService.getChallengeRecord(insightId));
    }

    @GetMapping("/my-page/{userId}")
    public ApiResponse<List<InsightMyPageResponse>> getInsightForMyPage(
            @PathVariable Long userId,
            @RequestParam(required = false) Long drawerId,
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit) {

        return ApiResponse.ok(insightQueryApiService.getInsightsForMyPage(userId, drawerId, CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/bookmark")
    public ApiResponse<List<InsightGetForHomeResponse>> getBookmarkedInsight(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(insightQueryApiService.getInsightForBookmark(CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/challenge/my")
    public ApiResponse<List<InsightGetForHomeResponse>> paginateInsightsOfChallenge(
            @RequestParam(required = false, defaultValue = KeeweConsts.LONG_MAX_STRING) Long cursor,
            @RequestParam Long limit,
            @RequestParam(required = false) Long writerId
    ) {
        return ApiResponse.ok(insightQueryApiService.paginateInsightsOfChallenge(CursorPageable.of(cursor, limit), writerId));
    }
}
