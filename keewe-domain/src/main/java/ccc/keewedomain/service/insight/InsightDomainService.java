package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void incrementViewCount(InsightViewIncrementDto dto) {
        mqPublishService.publish(KeeweConsts.INSIGHT_VIEW_EXCHANGE, String.valueOf(dto.getInsightId()));
    }

    @Transactional
    public Long incrementViewCount(Long insightId) {
        Insight insight = insightRepository.findByIdOrElseThrow(insightId);
        log.info("[IDS::incrementViewCount] Curr view {}, Next view {}", insight.getView(), insight.getView() + 1);
        return insight.incrementView();
    }

    //FIXME get과 find 역할 정확히 정리하기
    public Insight getById(Long id) {
        return insightRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR445));
    }
}
