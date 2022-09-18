package ccc.keewedomain.repository.insight;

import ccc.keewedomain.domain.insight.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InsightRepository extends JpaRepository<Insight, Long> {
}
