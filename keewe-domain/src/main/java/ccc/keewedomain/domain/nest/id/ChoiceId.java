package ccc.keewedomain.domain.nest.id;

import ccc.keewedomain.domain.nest.Candidate;
import ccc.keewedomain.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ChoiceId implements Serializable {
    private Candidate candidate;
    private User user;
}
