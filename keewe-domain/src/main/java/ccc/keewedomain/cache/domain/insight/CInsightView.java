package ccc.keewedomain.cache.domain.insight;

import ccc.keewecore.consts.KeeweConsts;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = KeeweConsts.INSIGHT_VIEW_HASH_KEY)
public class CInsightView {
    @Id
    private Long insightId;
    private Long viewCount;

    public static CInsightView of(Long insightId, Long viewCount) {
        return new CInsightView(insightId, viewCount);
    }
}