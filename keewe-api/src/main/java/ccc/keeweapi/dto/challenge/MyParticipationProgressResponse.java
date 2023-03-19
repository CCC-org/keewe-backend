package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MyParticipationProgressResponse {
    private String name;
    private Long current;
    private Long total;
    private boolean todayRecorded;
    private boolean weekCompleted;
}
