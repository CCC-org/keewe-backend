package ccc.keewedomain.service.report;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.report.Report;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.report.ReportRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.notification.SlackNotiService;
import ccc.keeweinfra.vo.Attachment;
import ccc.keeweinfra.vo.Field;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsightReportDomainServiceImpl implements ReportDomainService {
    private final ReportRepository reportRepository;
    private final InsightQueryDomainService insightQueryDomainService;
    private final UserDomainService userDomainService;
    private final SlackNotiService slackNotiService;

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Transactional
    @Override
    public Report save(ReportCreateDto reportCreateDto) {
        User reporter = userDomainService.getUserByIdOrElseThrow(reportCreateDto.getUserId());
        Insight insight = insightQueryDomainService.getByIdOrElseThrow(reportCreateDto.getTargetId());

        Report report = Report.of(
            reporter,
            reportCreateDto.getReportTarget(),
            insight.getId(),
            reportCreateDto.getReportType(),
            reportCreateDto.getReason()
        );

        slackNotiService.sendMessage(
                KeeweConsts.REPORT_NOTI_HEADER.concat("-").concat(profile.toUpperCase()),
                KeeweConsts.SLACK_REPORT_BOT_NAME,
                KeeweConsts.SLACK_REPORT_BOT_IMG,
                createNotificationAttachments(insight, reporter, report)
        );

        return reportRepository.save(report);
    }

    @Override
    public ReportTarget getReportTarget() {
        return ReportTarget.INSIGHT;
    }

    private List<Attachment> createNotificationAttachments(Insight insight, User reporter, Report report) {
        Field writerInfo = Field.of("작성자ID", insight.getWriter().getId().toString());
        Field reporterInfo = Field.of("신고자ID", reporter.getId().toString());
        Field insightInfo = Field.of("인사이트ID", insight.getId().toString());
        Field reportTypeInfo = Field.of("카테고리", report.getReportType().getReason());

        String insightContents = insight.getContents();
        String cuttedContents = insightContents.length() > 30 ? insightContents.substring(0, 30) : insightContents;
        Field insightContentsInfo = Field.of("인사이트본문", cuttedContents);

        List<Field> fields = List.of(writerInfo, reporterInfo, insightInfo, reportTypeInfo, insightContentsInfo);
        return List.of(Attachment.of("#D00000", fields));
    }
}
