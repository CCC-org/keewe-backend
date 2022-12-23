package ccc.keewecore.utils;

import ccc.keewecore.consts.TitleCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Message;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeeweTitleHeader {
    private TitleCategory category;
    private String userId;

    public static KeeweTitleHeader toHeader(Message message) {
        KeeweTitleHeader keeweTitleHeader = new KeeweTitleHeader();
        keeweTitleHeader.userId = KeeweStringUtils.getOrDefault(message.getMessageProperties().getHeader("user-id"), "");
        keeweTitleHeader.category = TitleCategory.valueOf(KeeweStringUtils.getOrDefault(message.getMessageProperties().getHeader("user-id"), ""));
        return keeweTitleHeader;
    }

}
