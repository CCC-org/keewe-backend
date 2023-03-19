package ccc.keeweapi.service.report;

import ccc.keeweapi.component.ReportAssembler;
import ccc.keeweapi.dto.report.CommentReportRequest;
import ccc.keeweapi.dto.report.InsightReportRequest;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.service.report.ReportDomainService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReportApiService {
    private final ReportAssembler reportAssembler;
    private final Map<ReportTarget, ReportDomainService> reportDomainServiceMap;

    public ReportApiService(List<ReportDomainService> reportDomainServices, ReportAssembler reportAssembler) {
        this.reportAssembler = reportAssembler;
        this.reportDomainServiceMap = reportDomainServices.stream()
                .collect(Collectors.toMap(it -> it.getReportTarget(), it -> it));
    }

    public void reportInsight(InsightReportRequest request) {
        ReportCreateDto dto = reportAssembler.toReportCreateDto(request.getReportType(), ReportTarget.INSIGHT, request.getInsightId(), request.getReason());
        reportDomainServiceMap.get(ReportTarget.INSIGHT).save(dto);
    }

    public void reportComment(CommentReportRequest request) {
        ReportCreateDto dto = reportAssembler.toReportCreateDto(request.getReportType(), ReportTarget.COMMENT, request.getCommentId(), request.getReason());
        reportDomainServiceMap.get(ReportTarget.COMMENT).save(dto);
    }
}
