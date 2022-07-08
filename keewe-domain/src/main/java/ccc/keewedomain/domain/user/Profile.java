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
import static ccc.keewedomain.domain.user.enums.ProfileStatus.*;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
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
    private String nickname; // 닉네임

    @Column(name = "link", unique = true, nullable = false)
    private String link; // 프로필 링크

    @Column(name = "privacy")
    @Enumerated(EnumType.STRING)
    private Privacy privacy;

    @OneToOne
    @JoinColumn(name = "profile_photo_id")
    private ProfilePhoto profilePhoto;

    @Column(name = "profile_status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    @ElementCollection
    @CollectionTable(name = "favorite_activities",joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "activity")
    @Enumerated(EnumType.STRING)
    private List<Activity> activities; // 나를 나타내는 키워드 목록

    @OneToMany(mappedBy = "follower", fetch = LAZY)
    private List<Buddy> followers; // 내가 팔로우하는 사람들

    @OneToMany(mappedBy = "followee", fetch = LAZY)
    private List<Buddy> followees; // 나를 팔로우하는 사람들

    @OneToMany(mappedBy = "profile", fetch = LAZY)
    private List<ProfileLink> socialLinks;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public static ProfileBuilder initBuild() {
        return Profile.builder()
                .nickname("")
                .link("")
                .privacy(PUBLIC)
                .profileStatus(LINK_NEEDED)
                .activities(new ArrayList<>())
                .followers(new ArrayList<>())
                .followees(new ArrayList<>())
                .socialLinks(new ArrayList<>())
                .deleted(false);
    }

    public void createLink(String link) {
        this.link = link;
        this.profileStatus = ACTIVITIES_NEEDED;
    }

    public void createNickname(String nickname) {
        isCreatingOrElseThrow();
        this.nickname = nickname;
        updateOrMaintainStatus(SOCIAL_LINK_NEEDED);
    }

    private void updateOrMaintainStatus(ProfileStatus newStatus) {
        ProfileStatus currentStatus = this.profileStatus;
        if (currentStatus.getOrder() < newStatus.getOrder()) {
            this.profileStatus = newStatus;
        }
    }

    private boolean isCreatingOrElseThrow() {
        if (this.profileStatus.getOrder() >= ACTIVE.getOrder()) {
            throw new IllegalStateException("프로필 생성이 이미 완료되었습니다.");
        }

        return true;
    }
}
