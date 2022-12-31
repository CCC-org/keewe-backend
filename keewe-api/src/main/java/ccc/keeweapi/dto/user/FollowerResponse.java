package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowerResponse {
    private Long id;
    private String name;
    private String imageURL;
    private String title;
    private boolean isFollow;
}
