package ccc.keewedomain.dto.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class ReactionAggregationGetDto {
    private Long clap;
    private Boolean isClapClicked;
    private Long heart;
    private Boolean isHeartClicked;
    private Long sad;
    private Boolean isSadClicked;
    private Long surprise;
    private Boolean isSurpriseClicked;
    private Long fire;
    private Boolean isFireClicked;
    private Long eyes;
    private Boolean isEyesClicked;

    public static ReactionAggregationGetDto createByCnt(CReactionCount cnt) {
        return ReactionAggregationGetDto.of(cnt.getClap(),
                false,
                cnt.getHeart(),
                false,
                cnt.getSad(),
                false,
                cnt.getSurprise(),
                false,
                cnt.getFire(),
                false,
                cnt.getEyes(),
                false
            );
    }

    public void updateClicked(List<Reaction> reactions) {
        boolean isClapClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.CLAP));
        boolean isHeartClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.HEART));
        boolean isSadClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.SAD));
        boolean isSurpriseClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.SURPRISE));
        boolean isFireClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.FIRE));
        boolean isEyesClicked = reactions.stream().anyMatch(reaction -> reaction.getType().equals(ReactionType.EYES));
        this.isClapClicked = isClapClicked;
        this.isHeartClicked = isHeartClicked;
        this.isSadClicked = isSadClicked;
        this.isSurpriseClicked = isSurpriseClicked;
        this.isFireClicked = isFireClicked;
        this.isEyesClicked = isEyesClicked;
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

    public Long getAllReactionCount() {
        return clap + heart + sad + surprise + fire + eyes;
    }
}
