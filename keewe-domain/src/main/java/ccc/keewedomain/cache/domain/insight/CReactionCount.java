package ccc.keewedomain.cache.domain.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Getter
@RedisHash(value = KeeweConsts.REACTION_COUNT_HASH_KEY)
public class CReactionCount {
    @Id
    private Long id;
    private Long clap;
    private Long heart;
    private Long sad;
    private Long surprise;
    private Long fire;
    private Long eyes;

    public void setByType(ReactionType type, Long cnt) {
        switch (type) {
            case CLAP:
                clap = cnt;
                return;
            case HEART:
                heart = cnt;
                return;
            case SAD:
                sad = cnt;
                return;
            case SURPRISE:
                surprise = cnt;
                return;
            case FIRE:
                fire = cnt;
                return;
            case EYES:
                eyes = cnt;
                return;
        }
    }
}
