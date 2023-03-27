package ccc.keeweapi.service.challenge.command;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.challenge.command.ChallengeCommandDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeParticipationCommandApiService {

    private final ChallengeCommandDomainService challengeCommandDomainService;

    @Transactional
    public void deleteChallenge() {
        User user = SecurityUtil.getUser();
        challengeCommandDomainService.exitCurrentChallengeIfExist(user);
    }
}
