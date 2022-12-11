package ccc.keewedomain.dto.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class ReactionAggregationGetDto {
    private Long clap;
    private Long heart;
    private Long sad;
    private Long surprise;
    private Long fire;
    private Long eyes;

    public static ReactionAggregationGetDto createByCnt(CReactionCount cnt) {
        return ReactionAggregationGetDto.of(cnt.getClap(), cnt.getHeart(), cnt.getSad(), cnt.getSurprise(), cnt.getFire(), cnt.getEyes());
    }

    public Long getByType(ReactionType reactionType) {
        Long value = -1L;
        switch (reactionType) {
            case CLAP:
                value = clap;
                break;
            case HEART:
                value =  heart;
                break;
            case SAD:
                value = sad;
                break;
            case SURPRISE:
                value = surprise;
                break;
            case FIRE:
                value = fire;
                break;
            case EYES:
                value = eyes;
                break;
        }
        return value;
    }
}
