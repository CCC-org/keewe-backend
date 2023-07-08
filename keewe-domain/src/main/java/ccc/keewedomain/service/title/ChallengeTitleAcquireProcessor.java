package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final ChallengeQueryDomainService challengeQueryDomainService;

    protected ChallengeTitleAcquireProcessor(
            MQPublishService mqPublishService,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository,
            ChallengeQueryDomainService challengeQueryDomainService,
            ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository);
        this.challengeQueryDomainService = challengeQueryDomainService;
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.CHALLENGE;
    }

    @Override
    protected List<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        Long challengeCount = challengeQueryDomainService.countByUserId(Long.valueOf(header.getUserId()));
        List<Long> titles = new ArrayList<>();
        if (challengeCount >= 1L) {
            titles.add(5000L);
        }
        if (challengeCount >= 2L) {
            titles.add(5003L);
        }
        return titles;
    }
}
