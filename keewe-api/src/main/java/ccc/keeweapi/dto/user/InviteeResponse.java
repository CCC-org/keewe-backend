package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class InviteeResponse {
    private Long userId;
    private String nickname;
    private String imageURL;
}
