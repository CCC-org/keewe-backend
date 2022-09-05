package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.User;

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

    public static VotePost from(User writer, String contents) {
        return new VotePost(writer, contents);
    }

    public VotePost(User writer, String contents) {
        super(writer, contents);
    }

    public void createCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
