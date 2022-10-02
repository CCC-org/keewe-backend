package ccc.keewedomain.persistence.domain.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.user.enums.Privacy;
import ccc.keewedomain.persistence.domain.user.enums.UserStatus;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.dto.user.UserSignUpDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "vendor_type")
    @Enumerated(value = EnumType.STRING)
    private VendorType vendorType;

    @Column(name = "email")
    private String email; // 아이디

    @Column(name = "password")
    private String password; // 비밀번호

    @Column(name = "phone_number", unique = true)
    private String phoneNumber; // 휴대전화 번호

    @Column(name = "nickname")
    private String nickname;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "profile_photo_id")
    private ProfilePhoto profilePhoto;

    @Column(name = "privacy")
    @Enumerated(EnumType.STRING)
    private Privacy privacy = Privacy.PUBLIC;

    @ElementCollection
    @CollectionTable(name = "favorite_interests", joinColumns = @JoinColumn(name = "user_id"))
    private List<Interest> interests = new ArrayList<>();

    //FIXME
//    @Column(name = "role")
//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private List<UserRole> role; // ~~~/profile/{닉네임}


    @OneToMany(mappedBy = "follower", fetch = LAZY)
    private List<Follow> followers = new ArrayList<>(); // 내가 팔로우하는 사람들

    @OneToMany(mappedBy = "followee", fetch = LAZY)
    private List<Follow> followees = new ArrayList<>(); // 나를 팔로우하는 사람들

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ONBOARD;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public static User from(UserSignUpDto userSignUpDto) {
        User user = new User();
        user.vendorId = userSignUpDto.getVendorId();
        user.vendorType = userSignUpDto.getVendorType();
        user.email = userSignUpDto.getEmail();
        user.password = userSignUpDto.getPassword();
        user.phoneNumber = userSignUpDto.getPhoneNumber();

        return user;
    }

    public void initProfile(String nickname, Set<String> interests) {
        verifyOnboardStatus();
        this.nickname = nickname;
        this.interests = interests.stream().map(Interest::of).collect(Collectors.toList());
        this.status = UserStatus.ACTIVE;
    }

    public void verifyOnboardStatus() {
        if (this.status != UserStatus.ONBOARD) {
            throw new KeeweException(KeeweRtnConsts.ERR427);
        }
    }
}

