package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.aop.annotations.BlockFilter;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.insight.ChallengeRecordResponse;
import ccc.keeweapi.dto.insight.InsightAuthorAreaResponse;
import ccc.keeweapi.dto.insight.InsightGetForHomeResponse;
import ccc.keeweapi.dto.insight.InsightGetResponse;
import ccc.keeweapi.dto.insight.InsightMyPageResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightQueryApiService {

    private final InsightQueryDomainService insightQueryDomainService;
    private final ProfileDomainService profileDomainService;
    private final InsightAssembler insightAssembler;
    private final ProfileAssembler profileAssembler;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;

    @Transactional(readOnly = true)
    public InsightGetResponse getInsight(Long insightId) {
        InsightGetDto insightGetDto = insightQueryDomainService.getInsight(insightAssembler.toInsightDetailDto(insightId));
        return insightAssembler.toInsightGetResponse(insightGetDto);
    }

    @Transactional(readOnly = true)
    public InsightAuthorAreaResponse getInsightAuthorAreaInfo(Long insightId) {
        Insight insight = insightQueryDomainService.getByIdWithWriter(insightId);
        boolean isFollowing = profileDomainService.getFollowingTargetIdSet(profileAssembler.toFollowCheckDto(insight.getWriter().getId()));
        return insightAssembler.toInsightAuthorAreaResponse(insight, isFollowing);
    }


    @Transactional(readOnly = true)
    @BlockFilter
    public List<InsightGetForHomeResponse> getInsightsForHome(CursorPageable<Long> cPage, Boolean follow) {
        return insightQueryDomainService.getInsightsForHome(SecurityUtil.getUser(), cPage, follow).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
    }

    // FIXME DTO 수정할 때 같이 네이밍 수정 필요
    @Transactional(readOnly = true)
    @BlockFilter
    public List<InsightGetForHomeResponse> paginateInsightsOfChallenge(CursorPageable<Long> cPage, Long writerId) {
        User user = SecurityUtil.getUser();
        Challenge challenge = challengeParticipateQueryDomainService.findCurrentParticipationByUserId(user.getId())
                .map(ChallengeParticipation::getChallenge)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));

        return insightQueryDomainService.getByChallenge(challenge, user, cPage, writerId).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public List<InsightGetForHomeResponse> getInsightForBookmark(CursorPageable<Long> cPage) {
        return insightQueryDomainService.getInsightForBookmark(SecurityUtil.getUser(), cPage).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
    }

    public ChallengeRecordResponse getChallengeRecord(Long insightId) {
        Insight insight = insightQueryDomainService.getByIdWithChallengeOrElseThrow(insightId);
        ChallengeParticipation participation = insight.getChallengeParticipation();

        if (Objects.isNull(participation)) {
            return null;
        }

        if (insight.isValid()) {
            Long order = insightQueryDomainService.getRecordOrder(participation, insightId);
            return insightAssembler.toChallengeRecordResponse(participation, order);
        }

        return insightAssembler.toChallengeRecordResponse(participation.getChallenge());
    }

    @Transactional(readOnly = true)
    public List<InsightMyPageResponse> getInsightsForMyPage(Long userId, Long drawerId, CursorPageable<Long> cPage) {
        return insightQueryDomainService.getInsightsForMyPage(SecurityUtil.getUser(), userId, drawerId, cPage).stream()
                .map(insightAssembler::toInsightMyPageResponse)
                .collect(Collectors.toList());
    }
}
