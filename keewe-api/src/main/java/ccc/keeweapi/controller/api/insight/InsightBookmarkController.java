package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.BookmarkToggleResponse;
import ccc.keeweapi.service.insight.InsightApiService;
import ccc.keewecore.aop.annotations.FLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class InsightBookmarkController {

    private final InsightApiService insightApiService;

    @PostMapping("/bookmark/{insightId}")
    @FLogging
    public ApiResponse<BookmarkToggleResponse> toggleBookmark(@PathVariable Long insightId) {
        return ApiResponse.ok(insightApiService.toggleInsightBookmark(insightId));
    }
}
