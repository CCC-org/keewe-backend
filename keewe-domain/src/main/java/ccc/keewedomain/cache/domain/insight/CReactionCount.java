package ccc.keewedomain.cache.domain.insight;

import ccc.keewecore.consts.KeeweConsts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = KeeweConsts.REACTION_COUNT_HASH_KEY)
public class CReactionCount {
    @Id
    private String id;
    private Long count;

    public static CReactionCount of(String id, Long count) {
        CReactionCount cReactionCount = new CReactionCount();
        cReactionCount.id = id;
        cReactionCount.count = count;

        return cReactionCount;
    }
}
