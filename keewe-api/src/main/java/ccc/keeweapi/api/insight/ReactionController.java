package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.ReactionRequest;
import ccc.keeweapi.dto.insight.ReactionResponse;
import ccc.keeweapi.service.insight.ReactionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insight")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionApiService insightApiService;

    @PostMapping("/reaction/react")
    public ApiResponse<ReactionResponse> react(@RequestBody ReactionRequest request) {
        return ApiResponse.ok(insightApiService.react(request));
    }

}
