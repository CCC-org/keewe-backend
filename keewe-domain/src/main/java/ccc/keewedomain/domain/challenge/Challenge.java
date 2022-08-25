package ccc.keewedomain.domain.challenge;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.common.Interest;
import ccc.keewedomain.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "challenge")
@Getter
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    private Interest interest;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "introduction", length = 150)
    private String introduction;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
