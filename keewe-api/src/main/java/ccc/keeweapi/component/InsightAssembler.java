package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.request.CommentCreateRequest;
import ccc.keeweapi.dto.insight.request.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.request.DrawerResponse;
import ccc.keeweapi.dto.insight.request.InsightCreateRequest;
import ccc.keeweapi.dto.insight.request.InsightUpdateRequest;
import ccc.keeweapi.dto.insight.response.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.*;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
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

    public InsightUpdateDto toInsightUpdateDto(InsightUpdateRequest request) {
        return InsightUpdateDto.of(SecurityUtil.getUserId(), request.getInsightId(), request.getContents(), request.getLink(), request.getDrawerId());
    }

    public InsightUpdateResponse toInsightUpdateResponse(Insight insight) {
        return InsightUpdateResponse.of(insight.getId());
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

    public InsightDetailDto toInsightDetailDto(Long insightId) {
        return InsightDetailDto.of(SecurityUtil.getUserId(), insightId);
    }

    public InsightGetResponse toInsightGetResponse(InsightGetDto dto) {
        return InsightGetResponse.of(
                dto.getId(),
                dto.getContents(),
                dto.getLink(),
                toReactionAggregationResponse(dto.getReaction()),
                dto.isBookmark(),
                dto.getDrawerId(),
                dto.getDrawerName()
        );
    }

    public InsightGetForHomeResponse toInsightGetForHomeResponse(InsightGetForHomeDto dto) {
        return InsightGetForHomeResponse.of(
                dto.getId(),
                dto.getContents(),
                dto.isBookmark(),
                dto.getLink(),
                toReactionAggregationResponse(dto.getReaction()),
                dto.getCreatedAt().toString(),
                dto.getWriter()
        );
    }

    public InsightGetForBookmarkedResponse toInsightGetForBookmarkedResponse(InsightGetForBookmarkedDto dto) {
        return InsightGetForBookmarkedResponse.of(
                dto.getId(),
                dto.getContents(),
                dto.isBookmark(),
                dto.getLink(),
                toReactionAggregationResponse(dto.getReaction()),
                dto.getCreatedAt().toString(),
                dto.getBookmarkedAt().toString(),
                dto.getWriter()
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

    public InsightAuthorAreaResponse toInsightAuthorAreaResponse(Insight insight, boolean isFollowing) {
        User writer = insight.getWriter();
        return InsightAuthorAreaResponse.of(
                writer.getId(),
                writer.getNickname(),
                writer.getRepTitleName(),
                writer.getInterests(),
                writer.getProfilePhotoURL(),
                isAuthor(insight),
                isFollowing,
                insight.getCreatedAt().toString()
        );
    }

    public BookmarkToggleResponse toBookmarkToggleResponse(boolean isBookmark) {
        return BookmarkToggleResponse.of(isBookmark);
    }

    public BookmarkToggleDto toBookmarkToggleDto(Long insightId) {
        return BookmarkToggleDto.of(SecurityUtil.getUserId(), insightId);
    }

    public ChallengeRecordResponse toChallengeRecordResponse(ChallengeParticipation participation, Long order) {
        Challenge challenge = participation.getChallenge();
        return ChallengeRecordResponse.of(challenge.getId(), challenge.getName(), order, participation.getTotalInsightNumber());
    }

    public ChallengeRecordResponse toChallengeRecordResponse(Challenge challenge) {
        return ChallengeRecordResponse.of(challenge.getId(), challenge.getName(), null, null);
    }

    public InsightMyPageResponse toInsightMyPageResponse(InsightMyPageDto dto) {
        return InsightMyPageResponse.of(
                dto.getId(),
                dto.getContents(),
                dto.getLink(),
                toReactionAggregationResponse(dto.getReaction()),
                dto.getCreatedAt().toString(),
                dto.isBookmark()
        );
    }

    public InsightDeleteDto toInsightDeleteDto(Long insightId) {
        return InsightDeleteDto.of(
                insightId,
                SecurityUtil.getUserId()
        );
    }

    public CommentDeleteDto toCommentDeleteDto(Long commentId) {
        return CommentDeleteDto.of(
                SecurityUtil.getUserId(),
                commentId
        );
    }

    public CommentDeleteResponse toCommentDeleteResponse(Long commentId) {
        return CommentDeleteResponse.of(commentId);
    }

    private boolean isAuthor(Insight insight) {
        return insight.getWriter().getId() == SecurityUtil.getUserId();
    }

    public InsightStatisticsResponse toInsightStatisticsResponse(
            Long viewCount,
            Long reactionCount,
            Long commentCount,
            Long bookmarkCount,
            Long shareCount
    ) {
        return InsightStatisticsResponse.of(viewCount, reactionCount, commentCount, bookmarkCount, shareCount);
    }
}
