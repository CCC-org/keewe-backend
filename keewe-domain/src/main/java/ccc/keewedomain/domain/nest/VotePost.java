package ccc.keewedomain.domain.nest;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("VOTE")
public class VotePost extends Post {

    @OneToMany(mappedBy = "votePost")
    private List<Candidate> candidates;
}
