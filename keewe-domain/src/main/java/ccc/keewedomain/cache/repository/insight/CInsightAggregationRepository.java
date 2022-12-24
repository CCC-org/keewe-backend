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
        redisTemplate.opsForValue().increment(createKeyFromUserId(userId));
    }

    public Long get(String userId) {
        String count = (String) redisTemplate.opsForValue().get(createKeyFromUserId(userId));
        return Long.valueOf(count);
    }

    public boolean saveIfAbsent(String userId) {
        return redisTemplate.opsForValue().setIfAbsent(createKeyFromUserId(userId), String.valueOf(0L));
    }

    public String createKeyFromUserId(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(TitleCategory.INSIGHT);
        sb.append("-");
        sb.append(userId);
        return new String(sb);
    }

}
