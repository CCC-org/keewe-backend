package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ChoiceId implements Serializable {
    private Candidate candidate;
    private Profile profile;
}
