package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightStatisticsResponse {
    private Long viewCount;
    private Long reactionCount;
    private Long commentCount;
    private Long bookmarkCount;
    private Long shareCount;
}
