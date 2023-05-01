package ccc.keeweapi.utils;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keeweapi.dto.InsightIdBlockRequest;
import ccc.keeweapi.dto.UserIdBlockRequest;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BlockedResourceManager {
    private final ProfileQueryDomainService profileQueryDomainService;
    private final InsightQueryDomainService insightQueryDomainService;

    public void validateAccessibleInsight(Long insightId) {
        Long writerId = insightQueryDomainService.getByIdWithWriter(insightId).getWriter().getId();
        validateUserIsBlocked(writerId);
    }

    public void validateAccessibleInsight(InsightIdBlockRequest request) {
        validateAccessibleInsight(request.getInsightId());
    }

    public void validateAccessibleUser(Long userId) {
        validateUserIsBlocked(userId);
    }

    public void validateAccessibleUser(UserIdBlockRequest request) {
        validateUserIsBlocked(request.getUserId());
    }

    public BlockFilteringResponse validateAccessibleUser(BlockFilteringResponse response) {
        validateUserIsBlocked(response.getUserId());
        return response;
    }

    public <T extends BlockFilteringResponse> List<T> filterBlockedUsers(List<T> responses) {
        if(responses == null || responses.isEmpty()) {
            return responses;
        }
        Set<Long> blockedUserIds = getBlockedUserIds();
        responses.removeIf(response -> blockedUserIds.contains(response.getUserId()));
        return responses;
    }

    private void validateUserIsBlocked(Long userId) {
        Set<Long> blockedUserIds = getBlockedUserIds();
        if (blockedUserIds.contains(userId)) {
            throw new KeeweException(KeeweRtnConsts.ERR453);
        }
    }

    private Set<Long> getBlockedUserIds() {
        Long userId = SecurityUtil.getUserId();
        return profileQueryDomainService.findBlockedUserIds(userId);
    }
}
