package ccc.keeweapi.component;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import org.springframework.stereotype.Component;

@Component
public class ReportAssembler {

    public ReportCreateDto toReportCreateDto(Long insightId, ReportType reportType, String reason) {
        return ReportCreateDto.of(SecurityUtil.getUserId(), insightId, reportType, reason);
    }
}
