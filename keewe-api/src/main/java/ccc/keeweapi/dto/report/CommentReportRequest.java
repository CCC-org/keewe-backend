package ccc.keeweapi.dto.report;


import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentReportRequest {
    private Long commentId;
    private ReportType reportType;
    private String reason;
}
