package ccc.keewecore.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Supplier;

@Component
public class KeeweTransactionManager {
    @Transactional
    public <T> T withTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
