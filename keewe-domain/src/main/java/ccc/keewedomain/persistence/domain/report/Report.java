package ccc.keewedomain.persistence.domain.report;

import static javax.persistence.FetchType.LAZY;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
import ccc.keewedomain.persistence.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = LAZY)
    private User reporter;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "report_target")
    private ReportTarget reportTarget;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    private String reason;

    public static Report of(User reporter, ReportTarget reportTarget, Long targetId, ReportType reportType, String reason) {
        Report report = new Report();
        report.reporter = reporter;
        report.reportTarget = reportTarget;
        report.targetId = targetId;
        report.reportType = reportType;
        report.reason = reason;
        return report;
    }
}
