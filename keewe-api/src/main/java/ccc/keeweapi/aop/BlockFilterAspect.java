package ccc.keeweapi.aop;

import ccc.keeweapi.aop.annotations.BlockFilter;
import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keeweapi.dto.InsightIdBlockRequest;
import ccc.keeweapi.dto.UserIdBlockRequest;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import java.util.List;
import java.util.Set;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BlockFilterAspect {
    private final ProfileQueryDomainService profileQueryDomainService;
    private final InsightQueryDomainService insightQueryDomainService;

    @Before(value = "@annotation(blockFilter) && args(insightId,..)")
    public void filterBlockedInsightWriter(BlockFilter blockFilter, Long insightId) {
        if(!blockFilter.insightWriterFilter()) {
            return;
        }
        validateWriterIsBlocked(insightId);
    }

    @Before(value = "@annotation(blockFilter) && args(request,..)")
    public void filterBlockedInsightWriter(BlockFilter blockFilter, InsightIdBlockRequest request) {
        if(!blockFilter.insightWriterFilter()) {
            return;
        }
        validateWriterIsBlocked(request.getInsightId());
    }

    @Before(value = "@annotation(blockFilter) && args(userId,..)")
    public void filterBlockedUserId(BlockFilter blockFilter, Long userId) {
        if(!blockFilter.userIdFilter()) {
            return;
        }
        validateUserIsBlocked(userId);
    }

    @Before(value = "@annotation(blockFilter) && args(request,..)")
    public void filterBlockedUserId(BlockFilter blockFilter, UserIdBlockRequest request) {
        if(!blockFilter.userIdFilter()) {
            return;
        }
        validateUserIsBlocked(request.getUserId());
    }

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "response")
    public BlockFilteringResponse filterBlockedUser(BlockFilteringResponse response) {
        validateUserIsBlocked(response.getUserId());
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

    private void validateWriterIsBlocked(Long insightId) {
        Long writerId = insightQueryDomainService.getByIdWithWriter(insightId).getWriter().getId();
        validateUserIsBlocked(writerId);
    }

    private void validateUserIsBlocked(Long response) {
        Set<Long> blockedUserIds = getBlockedUserIds();
        if (blockedUserIds.contains(response)) {
            throw new KeeweException(KeeweRtnConsts.ERR453);
        }
    }

    private Set<Long> getBlockedUserIds() {
        Long userId = SecurityUtil.getUserId();
        return profileQueryDomainService.findBlockedUserIds(userId);
    }
}
