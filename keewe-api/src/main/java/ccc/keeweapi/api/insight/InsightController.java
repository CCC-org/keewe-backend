package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.service.insight.InsightApiService;
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
            @RequestParam Long cursor,
            @RequestParam Long limit
    ) {
        return ApiResponse.ok(insightApiService.getInsightsForHome(CursorPageable.of(cursor, limit)));
    }

    @GetMapping("/author/{insightId}")
    public ApiResponse<InsightAuthorAreaResponse> getInsightAuthorAreaInfo(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.getInsightAuthorAreaInfo(insightId));
    }

    @PostMapping("/bookmark/{insightId}")
    public ApiResponse<BookmarkToggleResponse> toggleBookmark(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.toggleInsightBookmark(insightId));
    }
}
