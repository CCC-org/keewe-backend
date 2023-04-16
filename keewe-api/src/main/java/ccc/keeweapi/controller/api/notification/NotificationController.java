package ccc.keeweapi.controller.api.notification;

import static ccc.keewecore.consts.KeeweConsts.LONG_MAX_STRING;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keeweapi.dto.notification.PaginateNotificationResponse;
import ccc.keeweapi.service.notification.command.NotificationCommandApiService;
import ccc.keeweapi.service.notification.query.NotificationQueryApiService;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationQueryApiService notificationQueryApiService;
    private final NotificationCommandApiService notificationCommandApiService;

    @GetMapping
    public ApiResponse<PaginateNotificationResponse> paginateNotifications(
        @RequestParam(required = false, defaultValue = LONG_MAX_STRING) Long cursor,
        @RequestParam Long limit
    ) {
        return ApiResponse.ok(notificationQueryApiService.paginateNotifications(CursorPageable.of(cursor, limit)));
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> markAsReadToNotification(@PathVariable("notificationId") Long notificationId) {
        return ApiResponse.ok(notificationCommandApiService.markAsRead(notificationId));
    }
}
