package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.InsightCreateRequest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keeweapi.service.insight.InsightApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
