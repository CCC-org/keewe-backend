package ccc.keewedomain.event.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class ChallengeCreateEvent implements Serializable {
    private final Long userId;
    private final Long challengeId;
}
