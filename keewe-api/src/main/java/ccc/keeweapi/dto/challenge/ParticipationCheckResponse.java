package ccc.keeweapi.dto.challenge;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParticipationCheckResponse {
    private boolean participation;

    public static ParticipationCheckResponse of(boolean participation) {
        ParticipationCheckResponse response = new ParticipationCheckResponse();
        response.participation = participation;

        return response;
    }
}
