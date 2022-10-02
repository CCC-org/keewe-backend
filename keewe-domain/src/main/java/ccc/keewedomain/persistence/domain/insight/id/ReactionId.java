package ccc.keewedomain.persistence.domain.insight.id;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ReactionId implements Serializable {
    private Insight insight;
    private User reactor;
    private ReactionType type;
}
