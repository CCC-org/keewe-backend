package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.insight.QComment.comment;
import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.user.QProfilePhoto.profilePhoto;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    //최신순으로 limit 개의 댓글 조회 (답글X)
    public List<Comment> findByInsightIdOrderByIdDesc(Long insightId, CursorPageable<Long> cPage) {

        return queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .leftJoin(user.profilePhoto, profilePhoto)
                .fetchJoin()
                .where(comment.insight.id.eq(insightId)
                    .and(comment.parent.isNull())
                    .and(comment.id.gt(cPage.getCursor()))
                )
                .orderBy(comment.id.asc())
                .limit(cPage.getLimit())
                .fetch();
    }

    //최신순으로 limit 개의 댓글 조회 (답글X) - 차단된 유저의 댓글 제외
    public List<Comment> findByInsightIdOrderByIdDescWithoutBlocked(Long insightId, CursorPageable<Long> cPage, Collection<Long> blockedUserIds) {

        return queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .leftJoin(user.profilePhoto, profilePhoto)
                .fetchJoin()
                .where(comment.insight.id.eq(insightId)
                        .and(comment.parent.isNull())
                        .and(comment.id.gt(cPage.getCursor()))
                        .and(comment.writer.id.notIn(blockedUserIds))
                )
                .orderBy(comment.id.asc())
                .limit(cPage.getLimit())
                .fetch();
    }

    //답글이 많은 순서대로 limit 개의 댓글을 작성자와 함께 조회
    public List<Comment> findByReplyNumberDescWithUser(Long insightId, Long limit) {

        return queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(comment.id.in(parentIdsReplyDesc(insightId)))
                .limit(limit)
                .fetch();
    }

    //답글이 많은 순서대로 limit 개의 댓글 id 조회
    private JPQLQuery<Long> parentIdsReplyDesc(Long insightId) {

        return JPAExpressions.select(comment.parent.id)
                .from(comment)
                .where(comment.insight.id.eq(insightId))
                .groupBy(comment.parent.id)
                .orderBy(comment.id.count().desc());
    }

    //인사이트의 총 댓글 개수 조회
    public Long countByInsightId(Long insightId) {
        return queryFactory.select(comment.count())
                .from(comment)
                .where(comment.insight.id.eq(insightId))
                .fetchFirst();
    }

    //각 부모 댓글의 첫 답글들을 작성자와 함께 조회
    public Map<Long, Comment> findFirstRepliesWithWriter(List<Comment> parents, Collection<Long> blockedUserIds) {
        return queryFactory.from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(comment.id.in(findFirstReplyIds(parents, blockedUserIds)))
                .transform(GroupBy.groupBy(comment.parent.id).as(comment));
    }

    //최신순으로 cursor 답글 이전의 부모 댓글의 답글 n개를 작성자와 함께 조회
    public List<Comment> findRepliesWithWriter(Long parentId, CursorPageable<Long> cPage) {
        return queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(comment.id.in(findIdByParentIdAndCursorDesc(parentId, cPage)))
                .orderBy(comment.id.asc())
                .fetch();
    }

    public Map<Long, Long> getReplyNumbers(List<Comment> parents, Collection<Long> blockedUserIds) {
        return queryFactory
                .from(comment)
                .groupBy(comment.parent.id)
                .where(comment.parent.in(parents)
                        .and(comment.writer.id.notIn(blockedUserIds)))
                .transform(GroupBy.groupBy(comment.parent.id).as(comment.count()));
    }

    public Optional<Comment> findLatestByWriterOrderById(User writer, Long insightId) {
        return Optional.ofNullable(queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer, user)
                .fetchJoin()
                .leftJoin(user.profilePhoto, profilePhoto)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(comment.writer.eq(writer)
                        .and(comment.insight.id.eq(insightId))
                        .and(comment.parent.isNull())
                )
                .orderBy(comment.id.desc())
                .fetchFirst());
    }

    private List<Long> findIdByParentIdAndCursorDesc(Long parentId, CursorPageable<Long> cPage) {
        return queryFactory.select(comment.id)
                .from(comment)
                .where(comment.parent.id.eq(parentId)
                        .and(comment.id.lt(cPage.getCursor())))
                .orderBy(comment.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    //각 부모 댓글의 첫 답글의 id 조회
    private JPQLQuery<Long> findFirstReplyIds(List<Comment> parents, Collection<Long> blockedUserIds) {
        return JPAExpressions.select(comment.id.min())
                .from(comment)
                .groupBy(comment.parent.id)
                .where(comment.parent.in(parents)
                        .and(comment.writer.id.notIn(blockedUserIds))
                );
    }

    public List<Long> findIdsByUserIds(Long insightId, Collection<Long> blockedUserIds) {
        return queryFactory.select(comment.id)
                .from(comment)
                .where(comment.insight.id.eq(insightId))
                .where(comment.writer.id.in(blockedUserIds))
                .fetch();
    }

    public Long countByParentIds(List<Long> blockedUserCommentIds) {
        return queryFactory.select(comment.count())
                .from(comment)
                .where(comment.parent.id.in(blockedUserCommentIds))
                .fetchFirst();
    }

    public Long countRepliesByUserIds(Long insightId, Collection<Long> blockedUserIds) {
        return queryFactory.select(comment.count())
                .from(comment)
                .where(comment.insight.id.eq(insightId))
                .where(comment.parent.id.isNotNull())
                .where(comment.writer.id.in(blockedUserIds))
                .fetchFirst();
    }
}
