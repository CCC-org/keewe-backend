package ccc.keewedomain.service.insight.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.CommentQueryRepository;
import ccc.keewedomain.persistence.repository.insight.CommentRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentQueryDomainService {
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final ProfileQueryDomainService profileQueryDomainService;

    public Comment getByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR481));
    }

    public Optional<Comment> findByIdAndInsightId(Long id, Long insightId) {
        return commentRepository.findByIdAndInsightId(id, insightId);
    }

    public Long countByInsightId(Long insightId, Long userId) {
        Long commentCount = commentQueryRepository.countByInsightId(insightId);
        Set<Long> blockedUserIds = profileQueryDomainService.findBlockedUserIds(userId);
        if(blockedUserIds.isEmpty()) {
            return commentCount;
        }
        // 1. 차단한 유저의 댓글 제외 - 차단한 유저들이 작성한 댓글의 id 조회
        List<Long> blockedUserCommentIds = commentQueryRepository.findIdsByUserIds(insightId, blockedUserIds);
        // 2. 차단한 유저의 댓글에 달린 답글 제외 - 1에서 조회한 id가 parent인 댓글 개수 조회
        Long replyOnBlockedCommentCount = commentQueryRepository.countByParentIds(blockedUserCommentIds);
        // 3. 차단한 유저의 답글 수 조회 - 1, 2에서 중복으로 제거되는 개수 보완 목적
        Long dupBlockedReplyCount = commentQueryRepository.countRepliesByUserIds(insightId, blockedUserIds);
        // 4. 전체 개수에서 빼기
        return commentCount - blockedUserCommentIds.size() - replyOnBlockedCommentCount + dupBlockedReplyCount;
    }

    public Map<Long, Long> getReplyNumbers(List<Comment> parents) {
        return commentQueryRepository.getReplyNumbers(parents);
    }

    public List<Comment> getComments(Long insightId, CursorPageable<Long> cPage) {
        return commentQueryRepository.findByInsightIdOrderByIdDesc(insightId, cPage);
    }

    public List<Comment> getCommentsWithoutBlocked(Long insightId, CursorPageable<Long> cPage, Long userId) {
        Set<Long> blockedUserIds = profileQueryDomainService.findBlockedUserIds(userId);
        return commentQueryRepository.findByInsightIdOrderByIdDescWithoutBlocked(insightId, cPage, blockedUserIds);
    }

    public Optional<Comment> findLatestCommentByWriter(User writer, Long insightId) {
        return commentQueryRepository.findLatestByWriterOrderById(writer, insightId);
    }

    public Map<Long, Comment> getFirstReplies(List<Comment> parents) {
        return commentQueryRepository.findFirstRepliesWithWriter(parents);
    }

    public List<Comment> getReplies(Long parentId, CursorPageable<Long> cPage) {
        return commentQueryRepository.findRepliesWithWriter(parentId, cPage);
    }
}
