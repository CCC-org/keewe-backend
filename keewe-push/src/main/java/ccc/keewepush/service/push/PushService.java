package ccc.keewepush.service.push;

import ccc.keewedomain.service.user.query.UserTokenQueryDomainService;
import ccc.keewepush.dto.PushMessage;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushService {
    private final PushClient pushClient;
    private final UserTokenQueryDomainService userTokenQueryDomainService;

    public void sendPush(PushMessage message) {
        String pushToken;
        try {
            pushToken = userTokenQueryDomainService.findByIdOrElseThrow(message.getUserId()).getPushToken();
        } catch(RuntimeException e) {
            log.error("[PushService] errorMessage = [{}], userId = [{}]", e.getMessage(), message.getUserId());
            return;
        }

        if (!isValidPushToken(pushToken)) {
            log.error("[PushService] invalid push token. userId = [{}], token = [{}]", message.getUserId(), pushToken);
            return;
        }

        ExpoPushMessage expoPushMessage = getExpoPushMessage(message, pushToken);
        pushClient.sendPushNotificationsAsync(List.of(expoPushMessage))
                .orTimeout(5L, TimeUnit.SECONDS)
                .thenAcceptAsync(expoPushTickets -> expoPushTickets.forEach(this::logPushResult));
    }

    private boolean isValidPushToken(String pushToken) {
        return !Objects.isNull(pushToken) && PushClient.isExponentPushToken(pushToken);
    }

    private ExpoPushMessage getExpoPushMessage(PushMessage message, String pushToken) {
        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(pushToken);
        expoPushMessage.setTitle(message.getTitle());
        expoPushMessage.setBody(message.getContents());
        return expoPushMessage;
    }

    private void logPushResult(ExpoPushTicket ticket) {
        log.info("expo push result. id = [{}] status = [{}]", ticket.getId(), ticket.getStatus());
    }
}
