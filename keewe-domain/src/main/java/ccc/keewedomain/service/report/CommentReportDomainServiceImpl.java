package ccc.keewedomain.service.report;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.dto.insight.ReportCreateDto;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.insight.enums.ReportTarget;
import ccc.keewedomain.persistence.domain.report.Report;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.report.ReportRepository;
import ccc.keewedomain.service.insight.CommentDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.notification.SlackNotiService;
import ccc.keeweinfra.vo.Attachment;
import ccc.keeweinfra.vo.Field;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentReportDomainServiceImpl implements ReportDomainService {
    private final UserDomainService userDomainService;
    private final SlackNotiService slackNotiService;
    private final CommentDomainService commentDomainService;
    private final ReportRepository reportRepository;

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Override
    public Report save(ReportCreateDto reportCreateDto) {
        User reporter = userDomainService.getUserByIdOrElseThrow(reportCreateDto.getUserId());
        Comment comment = commentDomainService.getByIdOrElseThrow(reportCreateDto.getTargetId());

        Report report = Report.of(reporter,
                reportCreateDto.getReportTarget(),
                comment.getId(),
                reportCreateDto.getReportType(),
                reportCreateDto.getReason()
        );

        slackNotiService.sendMessage(
                KeeweConsts.REPORT_NOTI_HEADER.concat("-").concat(profile.toUpperCase()),
                KeeweConsts.SLACK_REPORT_BOT_NAME,
                KeeweConsts.SLACK_REPORT_BOT_IMG,
                createNotificationAttachments(comment, reporter, report)
        );

        return reportRepository.save(report);
    }

    @Override
    public ReportTarget getReportTarget() {
        return ReportTarget.COMMENT;
    }

    private List<Attachment> createNotificationAttachments(Comment comment, User reporter, Report report) {
        Field writerInfo = Field.of("작성자ID", comment.getWriter().getId().toString());
        Field reporterInfo = Field.of("신고자ID", reporter.getId().toString());
        Field insightInfo = Field.of("댓글ID", comment.getId().toString());
        Field reportTypeInfo = Field.of("신고카테고리", report.getReportType().getReason());

        String commentContents = comment.getContent();
        String cuttedContents = commentContents.length() > 30 ? commentContents.substring(0, 30) : commentContents;
        Field insightContentsInfo = Field.of("댓글본문", cuttedContents);

        List<Field> fields = List.of(writerInfo, reporterInfo, insightInfo, reportTypeInfo, insightContentsInfo);
        return List.of(Attachment.of("#D00000", fields));
    }
}
