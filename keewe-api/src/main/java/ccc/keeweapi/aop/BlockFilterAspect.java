package ccc.keeweapi.aop;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keeweapi.dto.InsightIdBlockRequest;
import ccc.keeweapi.dto.UserIdBlockRequest;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Aspect
@Component
public class BlockFilterAspect {
    public BlockFilterAspect(ProfileDomainService profileDomainService, InsightQueryDomainService insightQueryDomainService) {
        this.profileDomainService = profileDomainService;
        this.insightQueryDomainService = insightQueryDomainService;
    }

    private final ProfileDomainService profileDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ThreadLocal<Set<Long>> blockedUserIdsStore = new ThreadLocal<>();
    @Before(value = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter) && args(insightId,..)")
    public void filterBlockedInsightWriter(Long insightId) {
        validateInsightId(insightId);
    }

    @Before(value = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter) && args(request,..)")
    public void filterBlockedInsightWriter(InsightIdBlockRequest request) {
        validateInsightId(request.getInsightId());
    }

    @Before(value = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter) && args(userId,..)")
    public void filterBlockedUserId(Long userId) {
        validateUserId(userId);
    }

    @Before(value = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter) && args(request,..)")
    public void filterBlockedUserId(UserIdBlockRequest request) {
        validateUserId(request.getUserId());
    }

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "response")
    public BlockFilteringResponse filterBlockedUser(BlockFilteringResponse response) {
        validateUserId(response.getUserId());
        return response;
    }

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "returnValue")
    public List filterBlockedUser(List returnValue) {
        if(returnValue.isEmpty() || !(returnValue.get(0) instanceof BlockFilteringResponse)) {
            return returnValue;
        }
        List<BlockFilteringResponse> responses = returnValue;
        Set<Long> blockedUserIds = getBlockedUserIds();

        responses.removeIf(response -> blockedUserIds.contains(response.getUserId()));
        return responses;
    }

    private void validateInsightId(Long request) {
        Long writerId = insightQueryDomainService.getByIdWithWriter(request).getWriter().getId();
        validateUserId(writerId);
    }

    private void validateUserId(Long response) {
        Set<Long> blockedUserIds = getBlockedUserIds();
        if (blockedUserIds.contains(response)) {
            throw new KeeweException(KeeweRtnConsts.ERR453);
        }
    }

    private void initBlockedUserIds() {
        Long userId = SecurityUtil.getUserId();
        blockedUserIdsStore.set(profileDomainService.findBlockedUserIds(userId));
    }

    private Set<Long> getBlockedUserIds() {
        if(blockedUserIdsStore.get() == null) {
            initBlockedUserIds();
        }
        return blockedUserIdsStore.get();
    }
}
