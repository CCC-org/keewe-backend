package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Block;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.user.QBlock.block;
import static ccc.keewedomain.persistence.domain.user.QProfilePhoto.profilePhoto;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class BlockQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Block> findByUserId(Long userId) {
        return queryFactory
                .select(block)
                .from(block)
                .innerJoin(block.blockedUser, user)
                .fetchJoin()
                .leftJoin(user.profilePhoto, profilePhoto)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(block.user.id.eq(userId))
                .fetch();
    }
}
