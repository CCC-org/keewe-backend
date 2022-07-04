package ccc.keewedomain.domain.user;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static ccc.keewedomain.domain.user.enums.UserStatus.ACTIVE;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email; // 아이디

    @Column(name = "password")
    private String password; // 비밀번호

    @Column(name = "phone_number", unique = true)
    private String phoneNumber; // 휴대전화 번호

    //FIXME
//    @Column(name = "role")
//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private List<UserRole> role; // ~~~/profile/{닉네임}

    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<Profile> profiles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = ACTIVE;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}

