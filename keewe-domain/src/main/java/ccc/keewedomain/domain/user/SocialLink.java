package ccc.keewedomain.domain.user;


import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.common.Link;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "social_link")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLink extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_link_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Embedded
    private Link link;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public static SocialLink of(Profile profile, Link link) {
        SocialLink socialLink = new SocialLink();
        socialLink.profile = profile;
        socialLink.link = link;
        socialLink.deleted = false;

        return socialLink;
    }
}
