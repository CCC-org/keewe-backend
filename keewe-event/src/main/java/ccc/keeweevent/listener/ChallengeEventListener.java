package ccc.keeweevent.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.event.challenge.ChallengeCreateEvent;
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
public class ChallengeEventListener {
    private final MQPublishService mqPublishService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Async(value = "worker")
    public void handleEvent(ChallengeCreateEvent event) {
        KeeweTitleHeader followingMessageHeader = KeeweTitleHeader.of(TitleCategory.CHALLENGE, event.getUserId().toString());
        mqPublishService.publishWithEmptyMessage(KeeweConsts.DEFAULT_ROUTING_KEY, KeeweConsts.TITLE_STAT_QUEUE, followingMessageHeader::toMessageWithHeader);
    }
}
