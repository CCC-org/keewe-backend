package ccc.keeweapi.aop;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BlockFilterAspect {

    private final ProfileQueryDomainService profileQueryDomainService;

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "response")
    public BlockFilteringResponse filterBlockedUser(BlockFilteringResponse response) {
        Long userId = SecurityUtil.getUserId();
        Set<Long> blockedUserIds = profileQueryDomainService.findBlockedUserIds(userId);

        if(blockedUserIds.contains(response.getUserId())) {
            throw new KeeweException(KeeweRtnConsts.ERR453);
        }
        return response;
    }

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "returnValue")
    public List filterBlockedUser(List returnValue) {
        if(returnValue.isEmpty() || !(returnValue.get(0) instanceof BlockFilteringResponse)) {
            return returnValue;
        }
        List<BlockFilteringResponse> responses = returnValue;
        Long userId = SecurityUtil.getUserId();
        Set<Long> blockedUserIds = profileQueryDomainService.findBlockedUserIds(userId);

        responses.removeIf(response -> blockedUserIds.contains(response.getUserId()));
        return responses;
    }
}
