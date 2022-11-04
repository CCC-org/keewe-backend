package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.service.insight.InsightApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/author/{insightId}")
    public ApiResponse<InsightAuthorAreaResponse> getInsightAuthorAreaInfo(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getInsightAuthorAreaInfo(insightId));
    }

    @PostMapping("/bookmark/{insightId}")
    public ApiResponse<BookmarkToggleResponse> toggleBookmark(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.toggleInsightBookmark(insightId));
    }

    @GetMapping("/{insightId}/challenge-record")
    public ApiResponse<ChallengeRecordResponse> getChallengeRecord(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getChallengeRecord(insightId));
    }
}
