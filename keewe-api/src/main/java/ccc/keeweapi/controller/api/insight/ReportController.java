package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.ReportRequest;
import ccc.keeweapi.service.insight.ReportApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@RestController
public class ReportController {
    private final ReportApiService reportApiService;

    @PostMapping
    public ApiResponse<Void> reportInsight(@RequestBody ReportRequest request) {
        reportApiService.reportInsight(request);
        return ApiResponse.ok();
    }

}
