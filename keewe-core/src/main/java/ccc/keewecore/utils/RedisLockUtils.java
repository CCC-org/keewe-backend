package ccc.keewecore.utils;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.consts.LockType;
import ccc.keewecore.exception.KeeweException;
import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisLockUtils {
    private final RedisTemplate redisTemplate;

    public <T> T executeWithLock(LockType lockType, String key, Long expirationSeconds, Supplier<T> supplier) {
        try {
            lock(String.format(lockType.getKeyFormat(), key), expirationSeconds);
            return supplier.get();
        } catch (Exception ex) {
            throw ex;
        } finally {
            unlock(key);
        }
    }

    private void lock(String key, Long expirationSeconds) {
        final boolean result = redisTemplate.opsForValue().setIfAbsent(key, 1L, Duration.ofSeconds(expirationSeconds));
        if(!result) {
            throw new KeeweException(KeeweRtnConsts.ERR507);
        }
    }

    private void unlock(String key) {
        redisTemplate.delete(key);
    }
}
