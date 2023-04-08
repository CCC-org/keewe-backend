package ccc.keeweapi.aop;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class BlockFilterAspect {

    private final ProfileDomainService profileDomainService;

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "response")
    public BlockFilteringResponse filterBlockedUser(BlockFilteringResponse response) {
        Long userId = SecurityUtil.getUserId();
        Set<Long> blockedUserIds = profileDomainService.findBlockedUserIds(userId);

        if(blockedUserIds.contains(response.userId())) {
            return null;
        }
        return response;
    }

    @AfterReturning(pointcut = "@annotation(ccc.keeweapi.aop.annotations.BlockFilter)", returning = "returnValue")
    public List filterBlockedUser(List returnValue) {
        if(!(returnValue.get(0) instanceof BlockFilteringResponse)) {
            return returnValue;
        }
        List<BlockFilteringResponse> responses = returnValue;
        Long userId = SecurityUtil.getUserId();
        Set<Long> blockedUserIds = profileDomainService.findBlockedUserIds(userId);

        responses.removeIf(response -> blockedUserIds.contains(response.userId()));
        return responses;
    }
}
