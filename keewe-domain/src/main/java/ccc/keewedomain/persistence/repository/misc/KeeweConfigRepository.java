package ccc.keewedomain.persistence.repository.misc;

import ccc.keewedomain.persistence.domain.misc.KeeweConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeeweConfigRepository extends JpaRepository<KeeweConfig, String> {
}
