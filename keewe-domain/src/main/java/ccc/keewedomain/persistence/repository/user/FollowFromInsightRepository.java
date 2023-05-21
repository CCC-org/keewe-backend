package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.FollowFromInsight;
import ccc.keewedomain.persistence.domain.user.id.FollowFromInsightId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowFromInsightRepository extends JpaRepository<FollowFromInsight, FollowFromInsightId> {
}
