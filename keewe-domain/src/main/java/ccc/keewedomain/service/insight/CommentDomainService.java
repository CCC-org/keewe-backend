package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.insight.Comment;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.repository.insight.CommentRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    private final InsightDomainService insightDomainService;
    private final UserDomainService userDomainService;

    public Comment create(CommentCreateDto dto) {
        Insight insight = insightDomainService.getById(dto.getInsightId());
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getWriterId());

        Optional<Comment> optParent = findByIdAndInsightIdOrElseThrow(dto.getParentId(), dto.getInsightId());
        optParent.ifPresent(this::validateHasNoParent);
        Comment parent = optParent.orElse(null);

        Comment comment = Comment.of(insight, writer, parent, dto.getContent());
        return commentRepository.save(comment);
    }

    private Optional<Comment> findByIdAndInsightIdOrElseThrow(Long id, Long insightId) {
        return commentRepository.findByIdAndInsightId(id, insightId);
    }

    private void validateHasNoParent(Comment comment) {
        if (comment.getParent() != null) {
            throw new KeeweException(KeeweRtnConsts.ERR443);
        }
    }
}
