package ccc.keewedomain.repository.user;

import ccc.keewedomain.domain.user.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByIdAndUserIdAndDeletedFalse(Long id, Long userId);

    Boolean existsByLinkAndDeletedFalse(String link);

    default Profile findByIdAndUserIdAndDeletedFalseOrElseThrow(Long id, Long userId) {
        return findByIdAndUserIdAndDeletedFalse(id, userId).orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));
    }
}
