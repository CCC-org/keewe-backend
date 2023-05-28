package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.response.ProfileVisitFromInsightCountResponse;
import ccc.keeweapi.dto.user.FollowFromInsightCountResponse;
import ccc.keeweapi.service.insight.command.InsightStatisticsCommandApiService;
import ccc.keeweapi.service.insight.query.InsightStatisticsQueryApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class InsightStatisticsController {
    private final InsightStatisticsQueryApiService insightStatisticsQueryApiService;
    private final InsightStatisticsCommandApiService insightStatisticsCommandApiService;

    @GetMapping("/{insightId}/statistics/follow")
    public ApiResponse<FollowFromInsightCountResponse> countFollowFromInsight(@PathVariable Long insightId) {
        return ApiResponse.ok(insightStatisticsQueryApiService.countFollowFromInsight(insightId));
    }

    @PostMapping("/{insightId}/statistics/profile-visit")
    public ApiResponse<Void> publishProfileVisitFromInsightEvent(@PathVariable Long insightId) {
        insightStatisticsCommandApiService.publishProfileVisitFromInsightEvent(insightId);
        return ApiResponse.ok();
    }

    @GetMapping("/{insightId}/statistics/profile-visit")
    public ApiResponse<ProfileVisitFromInsightCountResponse> countProfileVisitFromInsight(@PathVariable Long insightId) {
        return ApiResponse.ok(insightStatisticsQueryApiService.countProfileVisitFromInsight(insightId));
    }
}
