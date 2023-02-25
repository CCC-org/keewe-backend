package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.CommentCreateRequest;
import ccc.keeweapi.dto.insight.CommentCreateResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.service.insight.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightCommentCommandApiService {

    private final CommentDomainService commentDomainService;
    private final InsightAssembler insightAssembler;

    @Transactional
    public CommentCreateResponse create(CommentCreateRequest request) {
        Comment comment = commentDomainService.create(insightAssembler.toCommentCreateDto(request));
        return insightAssembler.toCommentCreateResponse(comment);
    }
}
