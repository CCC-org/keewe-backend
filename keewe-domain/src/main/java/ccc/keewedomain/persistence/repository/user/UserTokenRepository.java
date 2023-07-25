package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
