package ccc.keewedomain.service.insight.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.dto.insight.CommentDeleteDto;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.CommentQueryRepository;
import ccc.keewedomain.persistence.repository.insight.CommentRepository;
import ccc.keewedomain.service.insight.query.CommentQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentCommandDomainService {
    private final CommentRepository commentRepository;
    private final InsightQueryDomainService insightQueryDomainService;
    private final UserDomainService userDomainService;
    private final CommentQueryDomainService commentQueryDomainService;

    public Comment create(CommentCreateDto dto) {
        Insight insight = insightQueryDomainService.getByIdOrElseThrow(dto.getInsightId());
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());

        Optional<Comment> optParent = commentQueryDomainService.findByIdAndInsightId(dto.getParentId(), dto.getInsightId());
        optParent.ifPresent(this::validateHasNoParent);
        Comment parent = optParent.orElse(null);

        Comment comment = Comment.of(insight, writer, parent, dto.getContent());
        return commentRepository.save(comment);
    }

    public Long delete(CommentDeleteDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() -> {
            throw new KeeweException(KeeweRtnConsts.ERR442);
        });

        if (!Objects.equals(comment.getWriter().getId(), dto.getUserId()))
            throw new KeeweException(KeeweRtnConsts.ERR448);

        comment.delete();
        return comment.getId();
    }

    private void validateHasNoParent(Comment comment) {
        if (comment.getParent() != null) {
            throw new KeeweException(KeeweRtnConsts.ERR443);
        }
    }
}
