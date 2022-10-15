package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.*;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.dto.insight.DrawerCreateDto;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.InsightViewIncrementDto;
import ccc.keewedomain.persistence.domain.user.User;
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

    public InsightGetResponse toInsightGetResponse(InsightGetDto dto) {
        return InsightGetResponse.of(
                dto.getId(),
                dto.getContents(),
                dto.getLink(),
                toReactionAggregationResponse(dto.getReaction())
        );
    }

    private ReactionAggregationResponse toReactionAggregationResponse(ReactionAggregationGetDto dto) {
        return ReactionAggregationResponse.of(
                dto.getClap(),
                dto.getHeart(),
                dto.getSad(),
                dto.getSurprise(),
                dto.getFire(),
                dto.getEyes()
        );
    }

    public DrawerResponse toDrawerResponse(Drawer drawer) {
        return DrawerResponse.of(drawer.getId(), drawer.getName());
    }

    public InsightAuthorAreaResponse toInsightAuthorAreaResponse(Insight insight) {
        User writer = insight.getWriter();
        return InsightAuthorAreaResponse.of(insight.getId(), writer.getNickname(), "title", writer.getInterests(), "www.api-keewe.com/images" , isAuthor(insight), insight.getCreatedAt().toString());
    }

    private boolean isAuthor(Insight insight) {
        return insight.getWriter().getId() == SecurityUtil.getUserId();
    }
}
