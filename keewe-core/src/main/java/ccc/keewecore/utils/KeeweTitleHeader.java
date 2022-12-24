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
        KeeweTitleHeader header = new KeeweTitleHeader();
        header.userId = KeeweStringUtils.getOrDefault(message.getMessageProperties().getHeader("user-id"), "");
        header.category = TitleCategory.valueOf(KeeweStringUtils.getOrDefault(message.getMessageProperties().getHeader("category"), ""));
        return header;
    }

}
