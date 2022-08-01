package ccc.keewedomain.repository.nest;

import ccc.keewedomain.domain.nest.Nest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NestRepository extends JpaRepository<Nest, Long> {
    Optional<Nest> findByProfileId(Long profileId);
}
