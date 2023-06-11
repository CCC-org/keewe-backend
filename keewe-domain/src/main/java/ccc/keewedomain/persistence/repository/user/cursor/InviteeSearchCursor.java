package ccc.keewedomain.persistence.repository.user.cursor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InviteeSearchCursor {
    private String nickname;
    private Long userId;

    public static InviteeSearchCursor from(String cursorStr) {
        if(cursorStr == null) {
            return new InviteeSearchCursor(null, null);
        }

        int lastIndex = cursorStr.lastIndexOf(":");
        String nickname = cursorStr.substring(0, lastIndex);
        Long userId = Long.valueOf(cursorStr.substring(lastIndex + 1));
        return new InviteeSearchCursor(nickname, userId);
    }

    public static InviteeSearchCursor of(String nickname, Long userId) {
        return new InviteeSearchCursor(nickname, userId);
    }

    @Override
    public String toString() {
        return nickname + ":" + userId;
    }
}
