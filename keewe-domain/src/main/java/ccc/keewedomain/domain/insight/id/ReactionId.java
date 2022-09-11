package ccc.keewedomain.domain.insight.id;

import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.user.User;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ReactionId {
    private Insight insight;
    private ReactionType type;
    private User user;
}
