package ccc.keeweapi.service.notification.query;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keeweapi.dto.notification.PaginateNotificationResponse;
import ccc.keeweapi.service.notification.NotificationProcessor;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.utils.ListUtils;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.notification.query.NotificationQueryDomainService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;

@Service
@Slf4j
public class NotificationQueryApiService {
    private final NotificationQueryDomainService notificationQueryDomainService;
    private final Map<NotificationCategory, NotificationProcessor> notificationProcessors;

    public NotificationQueryApiService(
            NotificationQueryDomainService notificationQueryDomainService,
            List<NotificationProcessor> notificationProcessors
    ) {
        this.notificationQueryDomainService = notificationQueryDomainService;
        this.notificationProcessors = notificationProcessors.stream()
                .collect(Collectors.toMap(NotificationProcessor::getCategory, notificationProcessor -> notificationProcessor));
    }

    @Transactional
    public PaginateNotificationResponse paginateNotifications(CursorPageable<Long> cPage) {
        List<NotificationResponse> notificationResponses = notificationQueryDomainService.paginateNotifications(cPage, SecurityUtil.getUser()).stream()
                .map(notification -> {
                    NotificationProcessor notificationProcessor = notificationProcessors.get(notification.getContents().getCategory());
                    Assert.notNull(notificationProcessor);
                    return notificationProcessor.process(notification);
                })
                .collect(Collectors.toList());

        if(notificationResponses.size() >= cPage.getLimit()) {
            NotificationResponse lastResponse = ListUtils.getLast(notificationResponses);
            return PaginateNotificationResponse.of(lastResponse.getId(), notificationResponses);
        } else {
            return PaginateNotificationResponse.of(null, notificationResponses);
        }
    }
}
