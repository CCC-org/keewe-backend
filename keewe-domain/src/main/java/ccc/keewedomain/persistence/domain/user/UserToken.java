package ccc.keewedomain.persistence.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class UserToken {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "push_token")
    private String pushToken;

    public void registerPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
