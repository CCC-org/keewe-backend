package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class RelatedUserResponse {
    private Long userId;
    private String nickname;
    private String imageURL;
}
