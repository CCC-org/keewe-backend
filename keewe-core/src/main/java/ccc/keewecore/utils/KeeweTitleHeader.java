package ccc.keewecore.utils;

import ccc.keewecore.consts.TitleCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class KeeweTitleHeader {
    private TitleCategory category;
    private String userId;

    public static KeeweTitleHeader toHeader(Message message) {
        KeeweTitleHeader header = new KeeweTitleHeader();
        MessageProperties messageProperties = message.getMessageProperties();
        header.userId = KeeweStringUtils.getOrDefault(messageProperties.getHeader("user-id"), "");

        String category = messageProperties.getHeader("category");
        if(StringUtils.hasText(category)) {
            header.category = TitleCategory.valueOf(KeeweStringUtils.getOrDefault(category, ""));
        }

        return header;
    }

    public Message toMessageWithHeader(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.setHeader("user-id", userId);
        messageProperties.setHeader("category", category);

        return message;
    }

}
