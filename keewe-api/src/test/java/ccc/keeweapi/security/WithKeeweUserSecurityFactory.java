package ccc.keeweapi.security;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.UserSignUpDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithKeeweUserSecurityFactory implements WithSecurityContextFactory<WithKeeweUser> {

    @Override
    public SecurityContext createSecurityContext(WithKeeweUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.from(UserSignUpDto.of(customUser.email(), null, null));
        UserPrincipal principal = new UserPrincipal(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities());

        context.setAuthentication(auth);

        return context;
    }
}
