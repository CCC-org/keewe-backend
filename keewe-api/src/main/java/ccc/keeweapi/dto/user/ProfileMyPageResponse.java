package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class ProfileMyPageResponse {

    private String nickname;
    private String image;
    private String title;
    private String introduction;
    private List<String> interests;
    private Boolean isFollow;
    private Long followerCount;
    private Long followingCount;
    private String challengeName;
}
