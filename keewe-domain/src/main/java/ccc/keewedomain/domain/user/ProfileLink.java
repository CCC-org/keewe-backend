package ccc.keewedomain.domain.user;


import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.common.Link;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "profile_link")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileLink extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_link_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Embedded
    private Link link;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public static ProfileLink of(Profile profile, Link link) {
        ProfileLink profileLink = new ProfileLink();
        profileLink.profile = profile;
        profileLink.link = link;
        profileLink.deleted = false;

        return profileLink;
    }
}
