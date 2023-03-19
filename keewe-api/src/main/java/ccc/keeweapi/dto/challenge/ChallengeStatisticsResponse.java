package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChallengeStatisticsResponse {
    private Long viewCount;
    private Long reactionCount;
    private Long commentCount;
    private Long bookmarkCount;
    private Long shareCount;
}
