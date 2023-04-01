package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ParticipatingChallengeResponse {
    private Long challengeId;
    private String name;
    private String interest;
    private String myTopic;
    private int insightPerWeek;
    private int duration;
    private String endDate;
    private String startDate;
}
