package ccc.keeweapi.service.challenge.query;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.OpenedChallengeResponse;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeQueryApiService {

    private final ChallengeAssembler challengeAssembler;
    private final ChallengeQueryDomainService challengeQueryDomainService;

    public List<OpenedChallengeResponse> paginate(CursorPageable<Long> cPage) {
        return challengeQueryDomainService.paginate(cPage).stream()
                .map(challengeAssembler::toOpenedChallengeResponse)
                .collect(Collectors.toList());
    }
}
