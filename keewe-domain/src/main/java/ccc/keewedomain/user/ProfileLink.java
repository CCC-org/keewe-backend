package ccc.keewedomain.user;

import ccc.keewedomain.common.BaseTimeEntity;
import ccc.keewedomain.common.Link;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "profile_link")
public class ProfileLink extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Embedded
    private Link link;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
