package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
