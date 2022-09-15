package ccc.keewedomain.repository.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVendorId(String vendorId);

    default User findByVendorIdOrElseThrow(String vendorId) {
        return findByVendorId(vendorId).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR411));
    }
}
