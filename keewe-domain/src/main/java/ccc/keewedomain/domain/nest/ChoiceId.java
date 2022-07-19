package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@EqualsAndHashCode
public class ChoiceId implements Serializable {
    private Candidate candidate;
    private Profile profile;
}
