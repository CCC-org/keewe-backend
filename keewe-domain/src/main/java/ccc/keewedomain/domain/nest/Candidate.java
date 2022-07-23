package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.common.BaseTimeEntity;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "candidate")
public class Candidate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;

    @Column(name = "content", nullable = false, length = 25)
    private String contents;

    @OneToMany(mappedBy = "candidate", fetch = LAZY)
    private List<Choice> choices;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vote_post_id", nullable = false)
    private VotePost votePost;

}
