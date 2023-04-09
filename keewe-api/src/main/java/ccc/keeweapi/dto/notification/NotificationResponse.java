package ccc.keeweapi.dto.notification;

import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class NotificationResponse {
    private Long id;
    private String title;
    private String contents;
    private NotificationCategory category;
    private String referenceId;
    private Boolean read;
}
