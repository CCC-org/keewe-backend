package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    Long countByFollowee(User followee);
    Long countByFollower(User follower);
    default void deleteByIdIfExists(FollowId id) {
        findById(id).ifPresent(this::delete);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followee = :user OR f.follower = :user")
    void deleteByFollowerIdOrFolloweeId(User user);
}
