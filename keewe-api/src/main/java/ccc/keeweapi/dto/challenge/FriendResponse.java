package ccc.keeweapi.dto.challenge;

import ccc.keeweapi.dto.BlockFilteringResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FriendResponse implements BlockFilteringResponse {
    private Long userId;
    private String nickname;
    private String imageURL;
    private long currentRecord;
    private long goalRecord;
    private boolean isFollowing;
}
