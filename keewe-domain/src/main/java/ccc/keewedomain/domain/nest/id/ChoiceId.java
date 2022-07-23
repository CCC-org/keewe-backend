package ccc.keewedomain.domain.nest.id;

import ccc.keewedomain.domain.nest.Candidate;
import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ChoiceId implements Serializable {
    private Candidate candidate;
    private Profile profile;
}
