package ccc.keewedomain.repository.insight;

import ccc.keewedomain.domain.insight.Drawer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawerRepository extends JpaRepository<Drawer, Long> {
    Optional<Drawer> findByIdAndAndDeletedFalse(Long id);
}
