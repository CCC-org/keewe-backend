package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChallengeProgressResponse {
    private String challengeName;
    private String challengeIntroduction;
    private Long current;
    private Long total;
    private int duration;
    private String startDate;
    private String endDate;
    private List<String> recordedDates;
}
