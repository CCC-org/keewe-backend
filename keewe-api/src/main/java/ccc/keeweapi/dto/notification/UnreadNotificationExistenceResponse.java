package ccc.keeweapi.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UnreadNotificationExistenceResponse {
    private boolean exist;
}
