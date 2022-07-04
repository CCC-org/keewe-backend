package ccc.keewedomain.repository.user;

import ccc.keewedomain.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
