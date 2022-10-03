package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.ReactionAssembler;
import ccc.keeweapi.dto.insight.ReactRequest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.service.insight.ReactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionApiService {
    private final ReactionAssembler reactionAssembler;
    private final ReactionDomainService reactionDomainService;

    public ReactResponse react(ReactRequest request) {
        ReactionDto reactionDto = reactionDomainService.react(reactionAssembler.toReactionIncrementDto(request));
        return reactionAssembler.toReactResponse(reactionDto);
    }
}
