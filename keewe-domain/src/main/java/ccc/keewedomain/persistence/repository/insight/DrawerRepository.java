package ccc.keewedomain.persistence.repository.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DrawerRepository extends JpaRepository<Drawer, Long>, DrawerQueryRepository {
    Optional<Drawer> findByIdAndDeletedFalse(Long id);
    List<Drawer> findAllByUserIdAndDeletedFalse(Long userId);

    default Drawer findByIdOrElseThrow(Long id) {
        return findByIdAndDeletedFalse(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR440));
    }
}
