package ccc.keeweinfra.dto;

import ccc.keeweinfra.vo.Attachment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SlackMessageRequest {
    private String text;
    private String username;
    private String iconEmoji;
    private List<Attachment> attachments;

    public static SlackMessageRequest of(String text, String username, String iconEmoji, List<Attachment> attachments) {
        SlackMessageRequest messageRequest = new SlackMessageRequest();
        messageRequest.text = text;
        messageRequest.username = username;
        messageRequest.iconEmoji = iconEmoji;
        messageRequest.attachments = attachments;
        return messageRequest;
    }
}
