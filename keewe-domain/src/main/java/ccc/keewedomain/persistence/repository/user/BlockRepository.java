package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.id.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
}
