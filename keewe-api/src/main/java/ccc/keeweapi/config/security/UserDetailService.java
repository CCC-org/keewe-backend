package ccc.keeweapi.config.security;

import ccc.keeweapi.exception.KeeweAuthException;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        return new UserPrincipal(user);
    }
}
