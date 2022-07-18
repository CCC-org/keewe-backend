package ccc.keewedomain.repository.user;

import ccc.keewedomain.domain.user.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
}
