package ccc.keewedomain.service.report;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Report;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.report.ReportRepository;
import ccc.keewedomain.service.insight.InsightDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.notification.SlackNotiService;
import ccc.keeweinfra.vo.Attachments;
import ccc.keeweinfra.vo.Fields;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportDomainService {
    private final ReportRepository reportRepository;
    private final InsightDomainService insightDomainService;
    private final UserDomainService userDomainService;
    private final SlackNotiService slackNotiService;

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Transactional
    public Report save(ReportCreateDto reportCreateDto) {
        User reporter = userDomainService.getUserByIdOrElseThrow(reportCreateDto.getUserId());
        Insight insight = insightDomainService.getByIdOrElseThrow(reportCreateDto.getInsightId());

        Report report = Report.of(reporter, insight, reportCreateDto.getReportType(), reportCreateDto.getReason());

        slackNotiService.sendMessage(
                KeeweConsts.INSIGHT_REPORT_NOTI_HEADER.concat("-").concat(profile.toUpperCase()),
                KeeweConsts.SLACK_REPORT_BOT_NAME,
                KeeweConsts.SLACK_REPORT_BOT_IMG,
                createNotificationAttachments(insight, report)
        );

        return reportRepository.save(report);
    }


    private List<Attachments> createNotificationAttachments(Insight insight, Report report) {
        Fields writerInfo = Fields.of("작성자", insight.getWriter().getNickname());
        Fields insightInfo = Fields.of("인사이트ID", insight.getId().toString());
        Fields reportTypeInfo = Fields.of("카테고리", report.getReportType().toString());

        String insightContents = insight.getContents();
        String cuttedContents = insightContents.length() > 30 ? insightContents.substring(0, 30) : insightContents;
        Fields insightContentsInfo = Fields.of("인사이트본문", cuttedContents);

        List<Fields> fields = List.of(writerInfo, insightInfo, reportTypeInfo, insightContentsInfo);
        return List.of(Attachments.of("#D00000", fields));
    }
}
