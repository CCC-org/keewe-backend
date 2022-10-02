package ccc.keewedomain.persistence.repository.nest;

import ccc.keewedomain.persistence.domain.nest.Nest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NestRepository extends JpaRepository<Nest, Long> {
}
