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

    public static VotePost from(Profile writer, String contents) {
        return new VotePost(writer.getNest(), writer, contents);
    }

    public VotePost(Nest nest, Profile writer, String contents) {
        super(nest, writer, contents);
    }

    public void createCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
