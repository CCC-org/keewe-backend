package ccc.keeweapi.service.insight;

import ccc.keeweapi.dto.insight.InsightAssembler;
import ccc.keeweapi.dto.insight.ReactionRequest;
import ccc.keeweapi.dto.insight.ReactionResponse;
import ccc.keewedomain.domain.insight.Reaction;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InsightApiService {
    private final InsightDomainService insightDomainService;
    private final InsightAssembler insightAssembler;

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    public ReactionResponse react(ReactionRequest request) {
        Reaction reaction = insightDomainService.react(insightAssembler.toReactionDto(request));
        return insightAssembler.toReactionResponse(reaction.getType());
    }
}
