package ccc.keewedomain.persistence.domain.insight;

import static javax.persistence.FetchType.LAZY;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
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

    @JoinColumn(name = "insight_id")
    @ManyToOne(fetch = LAZY)
    private Insight insight;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    private String reason;

    public static Report of(User reporter, Insight insight, ReportType reportType, String reason) {
        Report report = new Report();
        report.reporter = reporter;
        report.insight = insight;
        report.reportType = reportType;
        report.reason = reason;
        return report;
    }
}
