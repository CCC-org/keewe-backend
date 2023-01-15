package ccc.keeweapi.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReportRequest {
    private Long insightId;
    private ReportType reportType;
    private String reason;
}
