package ccc.keewedomain.service.title;

import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.TitleAcquireCheck;
import ccc.keewedomain.cache.repository.insight.CInsightAggregationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class TitleStatService {

    private final CInsightAggregationRepository insightAggregationRepository;
    private final ObjectProvider<TitleStatService> lazyTitleStatService;

    public void aggregate(Message message) {
        KeeweTitleHeader header = KeeweTitleHeader.toHeader(message);

        switch (header.getCategory()) {
            case INSIGHT:
                aggregateInsightCount(header.getUserId());
                break;
            default:


        }
    }

    private void aggregateInsightCount(String userId) {
        insightAggregationRepository.incrementInsightCount(userId);
        lazyTitleStatService.getIfAvailable().compareNumberEquals(insightAggregationRepository.get(userId), 5L, 10L, 50L, 100L);
    }


    @TitleAcquireCheck
    public boolean compareNumberEquals(Long target, Long... standards) {
        // 어떤 타이틀? 어떤 기준
        return Arrays.stream(standards).anyMatch(it -> it == target);
    }




}
