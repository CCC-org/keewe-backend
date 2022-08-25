package ccc.keeweapi.dto.challenge;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@ToString
public class ChallengeParticipateRequest {
    private Long challengeId;

    @Min(1) @Max(7)
    private int insightPerWeek;

    @Min(2) @Max(8)
    private int duration;

    @GraphemeLength(max = 150)
    private String myTopic;
}
