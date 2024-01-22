package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.response.BookmarkToggleResponse;
import ccc.keeweapi.service.insight.command.InsightCommandApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class InsightBookmarkController {

    private final InsightCommandApiService insightCommandApiService;

    @PostMapping("/bookmark/{insightId}")
    public ApiResponse<BookmarkToggleResponse> toggleBookmark(@PathVariable Long insightId) {
        return ApiResponse.ok(insightCommandApiService.toggleInsightBookmark(insightId));
    }
}
