package ccc.keewedomain.dto.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
            case HEART:
                value =  heart;
            case SAD:
                value = sad;
            case SURPRISE:
                value = surprise;
            case FIRE:
                value = fire;
            case EYES:
                value = eyes;
        }
        return value;
    }
}
