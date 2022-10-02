package ccc.keewedomain.persistence.domain.nest.id;

import ccc.keewedomain.persistence.domain.nest.Candidate;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ChoiceId implements Serializable {
    private Candidate candidate;
    private User user;
}
