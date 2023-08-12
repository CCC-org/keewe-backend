package ccc.keeweevent.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.event.follow.FollowCreateEvent;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowEventListener {
    private final MQPublishService mqPublishService;
    private final NotificationCommandDomainService notificationCommandDomainService;
    private final UserDomainService userDomainService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Async(value = "worker")
    public void handleEvent(FollowCreateEvent event) {
        Long userId = event.getFollowerId();
        Long targetId = event.getFolloweeId();
        log.info("[FollowEventListener] 팔로워 생성 이벤트 처리 - userId({}), targetUserId({})", userId, targetId);
        try {
            this.createNotification(userId, targetId);
            KeeweTitleHeader followeeMessageHeader = KeeweTitleHeader.of(TitleCategory.FOLLOWEE, targetId.toString());
            KeeweTitleHeader followingMessageHeader = KeeweTitleHeader.of(TitleCategory.FOLLOWING, userId.toString());
            mqPublishService.publishWithEmptyMessage(KeeweConsts.DEFAULT_ROUTING_KEY, KeeweConsts.TITLE_STAT_QUEUE, followeeMessageHeader::toMessageWithHeader);
            mqPublishService.publishWithEmptyMessage(KeeweConsts.DEFAULT_ROUTING_KEY, KeeweConsts.TITLE_STAT_QUEUE, followingMessageHeader::toMessageWithHeader);
            if (event.getRefInsightId() != null) {
                mqPublishService.publish(KeeweConsts.FOLLOW_FROM_INSIGHT_EXCHANGE, event);
            }
        } catch (Throwable t) {
            log.warn("[FollowEventListener] 팔로우 후 작업 실패 - user({}), target({})", userId, targetId, t);
        }
    }

    private void createNotification(Long userId, Long targetId) {
        User target = userDomainService.getUserByIdOrElseThrow(targetId);
        Notification notification = Notification.of(target, NotificationContents.팔로우, String.valueOf(userId));
        notificationCommandDomainService.save(notification);
        log.info("[FollowEventListener] 팔로워 생성 알람 생성 완료 - notificationTargetUser({})", targetId);
    }
}
