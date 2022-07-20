package ccc.keewedomain.domain.nest;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;

    @Column(name = "content", nullable = false, length = 140)
    private String contents;

    @OneToMany(mappedBy = "candidate")
    private List<Choice> choices;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vote_post_id", nullable = false)
    private VotePost votePost;

}
