package ccc.keeweapi.component;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import org.springframework.stereotype.Component;

@Component
public class ReportAssembler {

    public ReportCreateDto toReportCreateDto(
            ReportType reportType,
            ReportTarget reportTarget,
            Long targetId,
            String reason) {
        return ReportCreateDto.of(SecurityUtil.getUserId(), reportType, reportTarget, targetId, reason);
    }
}
