package ccc.keewedomain.service.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.domain.insight.id.CReactionCountId;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keeweinfra.service.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionDomainService {
    private final MQPublishService mqPublishService;
    private final CReactionCountRepository cReactionCountRepository;

    public ReactionDto react(ReactionIncrementDto dto) {
        Long reactionCount = getCurrentReactionCount(new CReactionCountId(dto.getInsightId(), dto.getReactionType()).toString())
                + dto.getValue();
        return ReactionDto.of(dto.getInsightId(), dto.getReactionType(), reactionCount);
    }

    private Long getCurrentReactionCount(String id) {
        CReactionCount cReactionCount = cReactionCountRepository.findById(id)
                .orElseGet(() -> {
                    log.info("[RDS::getReactionCount] No Reaction. id={}", id);
                    return CReactionCount.of(id, 0L);
                });
        return cReactionCount.getCount();
    }
}
