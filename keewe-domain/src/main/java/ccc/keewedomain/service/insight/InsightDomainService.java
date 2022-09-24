package ccc.keewedomain.service.insight;

import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.insight.Drawer;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.InsightViewIncrementDto;
import ccc.keewedomain.repository.insight.InsightRepository;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.MQPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightDomainService {

    private final InsightRepository insightRepository;
    private final MQPublishService mqPublishService;
    private final UserDomainService userDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final DrawerDomainService drawerDomainService;



    //TODO 참가한 챌린지에 기록하기
    public Insight create(InsightCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());
        Drawer drawer = drawerDomainService.findById(dto.getDrawerId()).orElse(null);
        ChallengeParticipation participation = challengeDomainService.findCurrentChallengeParticipation(dto.getWriterId())
                .orElse(null);

        Insight insight = Insight.of(writer, participation, drawer, dto.getContents(), Link.of(dto.getLink()));
        return insightRepository.save(insight);
    }

    public Long incrementViewCount(InsightViewIncrementDto dto) {
        //TODO redis 조회
        mqPublishService.publish(dto.getInsightId());
        return 3263L + 1;
    }
}
