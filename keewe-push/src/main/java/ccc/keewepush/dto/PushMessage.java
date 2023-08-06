package ccc.keewepush.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class PushMessage {
    private Long userId;
    private String title;
    private String contents;

    public static PushMessage of(Long userId, String contents) {
         return new PushMessage(userId, "Keewe", contents);
    }
}
