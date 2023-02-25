package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TogetherChallengerResponse {
    private String nickname;
    private String imageURL;
    private long current;
    private long total;
}
