package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MyBlockUserResponse {
    private Long id;
    private String nickname;
    private String title;
    private String imageURL;
}
