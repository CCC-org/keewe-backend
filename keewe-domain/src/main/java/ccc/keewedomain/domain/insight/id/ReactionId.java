package ccc.keewedomain.domain.insight.id;

import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ReactionId implements Serializable {
    private Insight insight;
    private User reactor;
    private ReactionType type;
}
