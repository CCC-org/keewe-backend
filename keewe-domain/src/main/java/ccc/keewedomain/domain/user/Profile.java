package ccc.keewedomain.domain.user;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.user.enums.Privacy;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static ccc.keewedomain.domain.user.enums.Privacy.PUBLIC;
import static ccc.keewedomain.domain.user.enums.ProfileStatus.LINK_NEEDED;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder(toBuilder = true)
@Getter
public class Profile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nickname", nullable = false)
    @Builder.Default
    private String nickname = ""; // 닉네임

    @Column(name = "link", unique = true, nullable = false)
    @Builder.Default
    private String link = ""; // 프로필 링크

    @Column(name = "privacy")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Privacy privacy = PUBLIC;

    @OneToOne
    @JoinColumn(name = "profile_photo_id")
    private ProfilePhoto profilePhoto;

    @Column(name = "profile_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProfileStatus profileStatus = LINK_NEEDED;

    @ElementCollection
    @CollectionTable(name = "favorite_activities",joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "activity")
    @Enumerated(EnumType.STRING)
    private List<Activity> activities = new ArrayList<>(); // 나를 나타내는 키워드 목록

    @OneToMany(mappedBy = "follower", fetch = LAZY)
    private List<Buddy> followers = new ArrayList<>(); // 내가 팔로우하는 사람들

    @OneToMany(mappedBy = "followee", fetch = LAZY)
    private List<Buddy> followees = new ArrayList<>(); // 나를 팔로우하는 사람들

    @OneToMany(mappedBy = "profile", fetch = LAZY)
    private List<ProfileLink> socialLinks = new ArrayList<>();

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    public ProfileBuilder mutate() {
        return this.toBuilder();
    }
}
