package ccc.keeweapi.utils;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {
    public static User getUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(ObjectUtils.isEmpty(principal.getUser())) {
            throw new KeeweException(KeeweRtnConsts.ERR404);
        }
        return principal.getUser();
    }

    public static Long getUserId() {
        return getUser().getId();
    }
}
