package ccc.keeweapi.dto.notification;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PaginateNotificationResponse {
    private Long nextCursor;
    private List<NotificationResponse> notifications;
}
