package ccc.keewedomain.repository.challenge;

import ccc.keewedomain.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
