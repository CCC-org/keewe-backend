package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.request.ReactRequest;
import ccc.keeweapi.dto.insight.response.ReactResponse;
import ccc.keeweapi.service.insight.command.InsightReactionCommandApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reaction")
@RestController
public class InsightReactionController {

    private final InsightReactionCommandApiService insightReactionCommandApiService;

    @PostMapping
    public ApiResponse<ReactResponse> react(@RequestBody ReactRequest request) {
        return ApiResponse.ok(insightReactionCommandApiService.react(request));
    }
}
