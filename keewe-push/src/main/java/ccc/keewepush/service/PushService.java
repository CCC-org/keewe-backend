package ccc.keewepush.service;

import ccc.keewedomain.service.user.query.UserTokenQueryDomainService;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushService {
    private final PushClient pushClient;
    private final UserTokenQueryDomainService userTokenQueryDomainService;


    public void sendPushByUserId(Long userId, String title, String content) {
        String pushToken = userTokenQueryDomainService.findByIdOrElseThrow(userId).getPushToken();

        if (!PushClient.isExponentPushToken(pushToken)) {
            log.error("[PushService] invalid push token. userId = [{}], token = [{}]", userId, pushToken);
        }

        ExpoPushMessage message = new ExpoPushMessage();
        message.getTo().add(pushToken);
        message.setTitle(title);
        message.setBody(content);

        pushClient.sendPushNotificationsAsync(List.of(message))
                .orTimeout(5L, TimeUnit.SECONDS)
                .thenAcceptAsync(expoPushTickets -> expoPushTickets.forEach(this::logPushResult));
    }

    private void logPushResult(ExpoPushTicket ticket) {
        log.info("expo push result. id = [{}] status = [{}]", ticket.getId(), ticket.getStatus());
    }
}
