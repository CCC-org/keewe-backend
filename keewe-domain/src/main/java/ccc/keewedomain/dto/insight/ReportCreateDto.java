package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReportCreateDto {
    private Long userId;
    private Long insightId;
    private ReportType reportType;
    private String reason;

    public static ReportCreateDto of(Long userId, Long insightId, ReportType reportType, String reason) {
        ReportCreateDto dto = new ReportCreateDto();
        dto.userId = userId;
        dto.insightId = insightId;
        dto.reportType = reportType;
        dto.reason = reason;
        return dto;
    }
}
