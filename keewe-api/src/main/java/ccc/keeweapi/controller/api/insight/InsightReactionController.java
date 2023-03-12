package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.ReactRequest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keeweapi.service.insight.command.InsightReactionCommandApiService;
import ccc.keeweapi.service.insight.query.InsightReactionQueryApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reaction")
@RestController
public class InsightReactionController {

    private final InsightReactionCommandApiService insightReactionCommandApiService;
    private final InsightReactionQueryApiService insightReactionQueryApiService;

    @PostMapping
    public ApiResponse<ReactResponse> react(@RequestBody ReactRequest request) {
        return ApiResponse.ok(insightReactionCommandApiService.react(request));
    }

    @GetMapping("/challenge-statistics")
    public ApiResponse<?> aggregateReactionOfCurrentChallenge() {
        return ApiResponse.ok(insightReactionQueryApiService.aggregatePerReactionType());
    }
}
