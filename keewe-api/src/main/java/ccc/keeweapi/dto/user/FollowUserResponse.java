package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowUserResponse {
    private Long id;
    private String nickname;
    private String imageURL;
    private String title;
    private boolean isFollow;
}
