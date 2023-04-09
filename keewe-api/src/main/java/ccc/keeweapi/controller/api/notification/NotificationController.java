package ccc.keeweapi.controller.api.notification;

import static ccc.keewecore.consts.KeeweConsts.LONG_MAX_STRING;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keeweapi.dto.notification.PaginateNotificationResponse;
import ccc.keeweapi.service.notification.command.NotificationCommandApiService;
import ccc.keeweapi.service.notification.query.NotificationQueryApiService;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        //return ApiResponse.ok(notificationQueryApiService.paginateNotifications(CursorPageable.of(cursor, limit)));
        return ApiResponse.ok(
                PaginateNotificationResponse.of(
                10L, List.of(
                        NotificationResponse.of(3L, "내 인사이트에 \n누군가 댓글 남김", "유승훈님이 댓글을 남겼어요.", NotificationCategory.COMMENT, "3", false),
                        NotificationResponse.of(4L, "초보 기록가", "꾸준함이 중요하죠. 초보 기록가!", NotificationCategory.TITLE, "6", true)
                ))
        );
    }

    @PostMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> markAsReadToNotification(@PathVariable("notificationId") Long notificationId) {
        return ApiResponse.ok(notificationCommandApiService.markAsRead(notificationId));
    }
}
