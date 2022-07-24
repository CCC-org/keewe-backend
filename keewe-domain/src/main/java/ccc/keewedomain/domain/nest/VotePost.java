package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("VOTE")
public class VotePost extends Post {

    @OneToMany(mappedBy = "votePost")
    private List<Candidate> candidates;

    protected VotePost(Nest nest, Profile writer, String content) {
        super(nest, writer, content);
    }
}
