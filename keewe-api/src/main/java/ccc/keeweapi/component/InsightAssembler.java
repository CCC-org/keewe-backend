package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.dto.insight.DrawerCreateDto;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.InsightViewIncrementDto;
import org.springframework.stereotype.Component;

@Component
public class InsightAssembler {

    public InsightCreateDto toInsightCreateDto(InsightCreateRequest request) {
        return InsightCreateDto.of(SecurityUtil.getUserId(),
                request.getContents(),
                request.getLink(),
                request.isParticipation(),
                request.getDrawerId());
    }

    public InsightCreateResponse toInsightCreateResponse(Insight insight) {
        return InsightCreateResponse.of(insight.getId());
    }

    public DrawerCreateDto toDrawerCreateDto(DrawerCreateRequest request) {
        return DrawerCreateDto.of(SecurityUtil.getUserId(), request.getName());
    }

    public DrawerCreateResponse toDrawerCreateResponse(Drawer drawer) {
        return DrawerCreateResponse.of(drawer.getId());
    }

    public InsightViewIncrementDto toInsightViewIncrementDto(Long insightId) {
        return InsightViewIncrementDto.of(insightId);
    }

    public InsightViewIncrementResponse toInsightViewIncrementResponse(Long viewCount) {
        return InsightViewIncrementResponse.of(viewCount);
    }

    public CommentCreateDto toCommentCreateDto(CommentCreateRequest request) {
        return CommentCreateDto.of(
                SecurityUtil.getUserId(),
                request.getInsightId(),
                request.getParentId(),
                request.getContent()
        );
    }

    public CommentCreateResponse toCommentCreateResponse(Comment comment) {
        return CommentCreateResponse.of(comment.getId());
    }
}
