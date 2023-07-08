package ccc.keewedomain.cache.repository.insight;

import ccc.keewecore.consts.TitleCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CInsightAggregationRepository {
    private final RedisTemplate redisTemplate;

    public void incrementInsightCount(String userId) {
        saveIfAbsent(userId);
        redisTemplate.opsForValue().increment(createInsightAggregateKey(userId));
    }

    public Long get(String userId) {
        String count = (String) redisTemplate.opsForValue().get(createInsightAggregateKey(userId));
        return Long.valueOf(count);
    }

    public void saveIfAbsent(String userId) {
        redisTemplate.opsForValue().setIfAbsent(createInsightAggregateKey(userId), String.valueOf(0L));
    }

    public String createInsightAggregateKey(String userId) {
        return TitleCategory.INSIGHT + "-" + userId;
    }
}
