package ccc.keewedomain.cache.repository.insight;

import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CNativeInsightViewRepository {
    private final RedisTemplate redisTemplate;

    //FIXME Native방식 쓸지 아직 모르겠음. 일단 구성만
    public <T> T findFieldById(Long insightId,
                           String field,
                           Class<T> clazz) {

         return clazz.cast(
                 redisTemplate.opsForHash().get(makeHashKey(KeeweConsts.INSIGHT_VIEW_HASH_KEY, String.valueOf(insightId)), field)
         );
    }

    private String makeHashKey(String setKey, String hashKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(setKey);
        sb.append(KeeweConsts.HASH_KEY_DELIMITER);
        sb.append(hashKey);
        return new String(sb);
    }

}
