package ccc.keewedomain.repository.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.VendorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVendorIdAndVendorType(String vendorId, VendorType vendorType);

    default User findByVendorIdOrElseThrow(String vendorId, VendorType vendorType) {
        return findByVendorIdAndVendorType(vendorId, vendorType).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR411));
    }
}
