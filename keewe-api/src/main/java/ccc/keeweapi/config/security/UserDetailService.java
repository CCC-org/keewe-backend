package ccc.keeweapi.config.security;

import ccc.keeweapi.exception.KeeweAuthException;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.user.UserDomainService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserDomainService userDomainService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userDomainService.getUserByIdOrElseThrow(Long.parseLong(userId));
        if (user.isDeleted()) {
            throw new KeeweAuthException(KeeweRtnConsts.ERR402);
        }
        if (!isRequestOnboarding() && user.isOnboarding()) {
            throw new KeeweAuthException(KeeweRtnConsts.ERR406);
        }
        return new UserPrincipal(user);
    }

    private boolean isRequestOnboarding() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        return HttpMethod.POST.name().equals(httpMethod)
                && "/api/v1/user/profile".equals(requestURI);
    }
}
