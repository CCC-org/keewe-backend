package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.ReportAssembler;
import ccc.keeweapi.dto.insight.ReportRequest;
import ccc.keewedomain.service.insight.ReportDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportApiService {
    private final ReportDomainService reportDomainService;
    private final ReportAssembler reportAssembler;

    public void reportInsight(ReportRequest request) {
        reportDomainService.save(reportAssembler.toReportCreateDto(request.getInsightId(), request.getReportType(), request.getReason()));
    }

}
