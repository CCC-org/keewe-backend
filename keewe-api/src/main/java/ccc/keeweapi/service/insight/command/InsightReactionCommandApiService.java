package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.ReactionAssembler;
import ccc.keeweapi.dto.insight.ReactRequest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keeweapi.utils.BlockUtil;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.service.insight.ReactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightReactionCommandApiService {
    private final ReactionAssembler reactionAssembler;
    private final ReactionDomainService reactionDomainService;
    private final BlockUtil blockUtil;

    public ReactResponse react(ReactRequest request) {
        blockUtil.checkInsightWriter(request);
        ReactionDto reactionDto = reactionDomainService.react(reactionAssembler.toReactionIncrementDto(request));
        return reactionAssembler.toReactResponse(reactionDto);
    }
}
