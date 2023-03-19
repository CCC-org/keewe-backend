package ccc.keeweapi.controller.api.report;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.report.CommentReportRequest;
import ccc.keeweapi.dto.report.InsightReportRequest;
import ccc.keeweapi.service.report.ReportApiService;
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

    @PostMapping("/insight")
    public ApiResponse<Void> reportInsight(@RequestBody InsightReportRequest request) {
        reportApiService.reportInsight(request);
        return ApiResponse.ok();
    }

    @PostMapping("/comment")
    public ApiResponse<Void> reportComment(@RequestBody CommentReportRequest request) {
        reportApiService.reportComment(request);
        return ApiResponse.ok();
    }
}
