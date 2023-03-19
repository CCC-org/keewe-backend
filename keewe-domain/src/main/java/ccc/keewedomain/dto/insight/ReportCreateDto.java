package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReportCreateDto {
    private Long userId;
    private ReportType reportType;
    private ReportTarget reportTarget;
    private Long targetId;
    private String reason;

    public static ReportCreateDto of(
        Long userId,
        ReportType reportType,
        ReportTarget reportTarget,
        Long targetId,
        String reason
    ) {
        ReportCreateDto dto = new ReportCreateDto();
        dto.userId = userId;
        dto.reportType = reportType;
        dto.reportTarget = reportTarget;
        dto.targetId = targetId;
        dto.reason = reason;
        return dto;
    }
}
