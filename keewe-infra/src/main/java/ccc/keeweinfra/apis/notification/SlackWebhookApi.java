package ccc.keeweinfra.apis.notification;


import ccc.keeweinfra.dto.SlackMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slackapi", url = "${slack.url}")
public interface SlackWebhookApi {

    @PostMapping("/services/{workspaceId}/{channelId}/{slackKey}")
    void sendMessage(
            @PathVariable(name = "workspaceId") String workspaceId,
            @PathVariable(name = "channelId") String channelId,
            @PathVariable(name = "slackKey") String slackKey,
            @RequestBody SlackMessageRequest slackMessageRequest
    );
}
