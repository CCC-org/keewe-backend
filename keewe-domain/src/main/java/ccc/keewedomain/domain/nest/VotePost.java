package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Entity
@DiscriminatorValue("VOTE")
public class VotePost extends Post {

    @OneToMany(mappedBy = "votePost", cascade = PERSIST)
    private List<Candidate> candidates;

    public static VotePost from(Profile writer, String content) {
        return new VotePost(writer.getNest(), writer, content);
    }

    public VotePost(Nest nest, Profile writer, String content) {
        super(nest, writer, content);
    }

    public void createCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
