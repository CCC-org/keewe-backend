package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.Drawer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawerRepository extends JpaRepository<Drawer, Long>, DrawerQueryRepository {
    Optional<Drawer> findByIdAndAndDeletedFalse(Long id);
}
