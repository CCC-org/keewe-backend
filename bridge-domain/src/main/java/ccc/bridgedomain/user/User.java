package ccc.bridgedomain.user;

import ccc.bridgedomain.user.enums.UserStatus;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "user")
public class User {

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
    private List<Profile> profiles;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}

