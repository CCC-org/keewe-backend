package ccc.bridgedomain.user;

import ccc.bridgedomain.user.enums.Privacy;
import ccc.bridgedomain.common.enums.Activity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "profile")
public class Profile {

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

}
