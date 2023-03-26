package ccc.keewedomain.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ParticipationUpdateDto {
    private String myTopic;
    private int insightPerWeek;
    private int duration;
}
