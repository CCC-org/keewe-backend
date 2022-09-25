package ccc.keewedomain.repository.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.insight.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface InsightRepository extends JpaRepository<Insight, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query(value = "select i from Insight i where i.id = :id")
    Optional<Insight> findById(Long id);

    default Insight findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }
}
