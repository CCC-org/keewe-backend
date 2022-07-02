package ccc.bridgedomain.user;

import ccc.bridgedomain.common.Link;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "profile_link")
public class ProfileLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Embedded
    private Link link;
}
