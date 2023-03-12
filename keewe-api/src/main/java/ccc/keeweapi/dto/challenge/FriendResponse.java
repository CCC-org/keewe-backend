package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FriendResponse {
    private String nickname;
    private String imageURL;
    private long currentRecord;
    private long goalRecord;
    private boolean isFollowing;
}
