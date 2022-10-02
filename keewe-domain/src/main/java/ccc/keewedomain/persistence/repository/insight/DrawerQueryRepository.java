package ccc.keewedomain.persistence.repository.insight;

public interface DrawerQueryRepository {
    boolean existsByUserIdAndName(Long userId, String name);
}
