package ccc.keewedomain.service.report;

import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.report.Report;

public interface ReportDomainService {
    Report save(ReportCreateDto reportCreateDto);

    ReportTarget getReportTarget();
}
