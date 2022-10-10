package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
