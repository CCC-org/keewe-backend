package ccc.keewedomain.cache.domain.insight;

import ccc.keewecore.consts.KeeweConsts;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Getter
@RedisHash(value = KeeweConsts.FIRST_REACTION_HASH_KEY)
public class CFirstReaction {
    @Id
    Long id;
}
