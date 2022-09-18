package ccc.keewedomain.repository.insight;

import ccc.keewedomain.domain.insight.Reaction;
import ccc.keewedomain.domain.insight.id.ReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {
}
