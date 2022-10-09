package ccc.keewedomain.persistence.repository.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.insight.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface InsightRepository extends JpaRepository<Insight, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select i from Insight i where i.id = :id")
    Optional<Insight> findByIdWithReadWriteLock(@Param("id") Long id);

    default Insight findByIdWithLockOrElseThrow(Long id) {
        return findByIdWithReadWriteLock(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }
}
