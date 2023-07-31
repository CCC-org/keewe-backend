package ccc.keewedomain.cache.repository.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCountForTitle;
import org.springframework.data.repository.CrudRepository;

public interface CReactionCountTitleRepository extends CrudRepository<CReactionCountForTitle, Long> {

    default CReactionCountForTitle findByIdWithMissHandle(Long id) {
        return findById(id).orElseGet(() -> {
            CReactionCountForTitle cReactionCountForTitle = CReactionCountForTitle.of(id, 0L);
            save(cReactionCountForTitle);
            return cReactionCountForTitle;
        });
    }
}
