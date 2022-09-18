package ccc.keewedomain.repository.insight;

public interface DrawerRepositoryCustom {
    boolean existsByUserIdAndName(Long userId, String name);
}
