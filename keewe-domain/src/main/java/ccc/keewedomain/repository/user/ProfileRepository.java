package ccc.keewedomain.repository.user;

import ccc.keewedomain.domain.user.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
