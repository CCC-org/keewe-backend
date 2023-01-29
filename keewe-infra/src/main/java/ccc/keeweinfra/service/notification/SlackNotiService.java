package ccc.keeweinfra.service.notification;

import ccc.keeweinfra.apis.notification.SlackWebhookApi;
import ccc.keeweinfra.dto.SlackMessageRequest;
import ccc.keeweinfra.vo.Attachment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackNotiService {
    private final SlackWebhookApi slackWebhookApi;

    @Value("${slack.workspaceId}")
    private String workspaceId;

    @Value("${slack.channelId}")
    private String channelId;

    @Value("${slack.slackKey}")
    private String slackKey;

    @Async(value = "worker")
    public void sendMessage(String text, String username, String iconEmoji, List<Attachment> attachments) {
        SlackMessageRequest message = SlackMessageRequest.of(text, username, iconEmoji, attachments);
        log.info("[sendMessage] SlackMessage send done. {}", username);
        slackWebhookApi.sendMessage(workspaceId, channelId, slackKey, message);
    }
}
