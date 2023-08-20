package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.response.CommentCreateResponse;
import ccc.keeweapi.dto.insight.response.CommentDeleteResponse;
import ccc.keeweapi.dto.insight.response.ActiveCommentResponse;
import ccc.keeweapi.dto.insight.response.CommentWriterResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.ActivePreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ActiveReplyResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.service.insight.command.InsightCommentCommandApiService;
import ccc.keeweapi.service.insight.query.InsightCommentQueryApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

public class InsightCommentControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightCommentController insightCommentController;

    @Mock
    InsightCommentCommandApiService insightCommentCommandApiService;

    @Mock
    InsightCommentQueryApiService insightCommentQueryApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(insightCommentController, provider);
    }

    private CommentWriterResponse writer1 = CommentWriterResponse.of(1L, "유승훈", "타이틀1", "www.api-keewe.com/images");
    private CommentWriterResponse writer2 = CommentWriterResponse.of(2L, "최지훈", "타이틀2", "www.api-keewe.com/images");

    @Test
    @DisplayName("댓글 등록 API")
    void create_comment() throws Exception {
        String content = "댓글 내용";
        Long insightId = 1L;
        Long parentId = 1L;
        CommentCreateResponse response = CommentCreateResponse.of(
                1L,
                content,
                LocalDateTime.now().toString(),
                CommentWriterResponse.of(1L, "닉네임", "타이틀", "이미지 URL"),
                List.of(),
                0L
        );
        JSONObject commentCreateRequest = new JSONObject();
        commentCreateRequest
                .put("content", content)
                .put("insightId", insightId)
                .put("parentId", parentId);

        when(insightCommentCommandApiService.create(any())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/comments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(commentCreateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("댓글 등록 API 입니다.")
                        .summary("댓글 등록 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("content").description("댓글의 내용 최소 1자 최대 140자"),
                                fieldWithPath("insightId").description("댓글을 작성할 인사이트의 ID"),
                                fieldWithPath("parentId").description("답글을 달 대상 댓글(optional)").optional())
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.id").description("댓글의 id"),
                                fieldWithPath("data.content").description("댓글의 내용"),
                                fieldWithPath("data.createdAt").description("댓글의 생성 시각"),
                                fieldWithPath("data.totalReply").description("이 댓글의 답글 개수"),
                                fieldWithPath("data.writer").description("작성자의 정보(optional)").optional(),
                                fieldWithPath("data.writer.id").description("작성자의 id"),
                                fieldWithPath("data.writer.name").description("닉네임"),
                                fieldWithPath("data.writer.title").description("타이틀"),
                                fieldWithPath("data.writer.image").description("프로필 사진"),
                                fieldWithPath("data.replies[]").description("댓글의 답글 목록(빈 배열)").optional())
                        .tag("InsightComment")
                        .build()
        )));
    }

    @Test
    @DisplayName("댓글 삭제 API")
    void delete_comment_test() throws Exception {
        Long commentId = 1L;

        when(insightCommentCommandApiService.delete(commentId))
                .thenReturn(CommentDeleteResponse.of(commentId));

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/comments/{commentId}", commentId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("댓글 삭제 API 입니다.")
                        .summary("댓글 삭제 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(
                                parameterWithName("commentId").description("대상 댓글 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.commentId").description("삭제된 댓글(답글) ID"))
                        .tag("InsightComment")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 댓글 미리보기 조회 API")
    void get_representative_comment() throws Exception {
        Long insightId = 1L;
        String now = LocalDateTime.now().toString();

        List<PreviewCommentResponse> response = List.of(
                ActivePreviewCommentResponse.of(1L, writer1, "댓글1 내용", now),
                ActivePreviewCommentResponse.of(2L, writer2, "댓글2 내용", now),
                ActivePreviewCommentResponse.of(3L, writer1, "댓글3 내용", now)
        );

        when(insightCommentQueryApiService.getPreviewComments(insightId)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comments/insights/{insightId}/preview", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 댓글 미리보기 조회 API 입니다.")
                        .summary("인사이트 댓글 미리보기 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(parameterWithName("insightId").description("대상 인사이트의 id"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("댓글의 id"),
                                fieldWithPath("data[].content").description("댓글의 내용"),
                                fieldWithPath("data[].createdAt").description("댓글의 생성 시각"),
                                fieldWithPath("data[].writer.id").description("작성자의 id"),
                                fieldWithPath("data[].writer.name").description("닉네임"),
                                fieldWithPath("data[].writer.title").description("타이틀"),
                                fieldWithPath("data[].writer.image").description("프로필 사진")
                        )
                        .tag("InsightComment")
                        .build()
        )));
    }

    @Test
    @DisplayName("댓글 조회 API")
    void get_comments() throws Exception {
        Long insightId = 1L;
        Long totalReply = 2L;
        long cursor = 0L;
        long limit = 2L;
        String now = LocalDateTime.now().toString();

        ActiveReplyResponse reply1 = ActiveReplyResponse.of(writer2, 3L, 1L, "답글1 내용", now);
        ActiveReplyResponse reply2 = ActiveReplyResponse.of(writer2, 4L, 1L, "답글2 내용", now);
        ActiveCommentResponse comment1 = ActiveCommentResponse.of(1L, writer1, "댓글의 내용1", now, List.of(reply1), totalReply);
        ActiveCommentResponse comment2 = ActiveCommentResponse.of(2L, writer1, "댓글의 내용2", now, List.of(reply2), totalReply);

        when(insightCommentQueryApiService.getCommentsWithFirstReply(any(), any())).thenReturn(List.of(comment1, comment2));

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comments/insights/{insightId}", insightId)
                        .param("cursor", Long.toString(cursor))
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("댓글 목록 조회 API 입니다.")
                        .summary("댓글 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(parameterWithName("insightId").description("대상 인사이트의 id"))
                        .requestParameters(
                                parameterWithName("cursor").description("대상 답글의 id"),
                                parameterWithName("limit").description("가져올 답글의 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("댓글의 id"),
                                fieldWithPath("data[].content").description("댓글의 내용"),
                                fieldWithPath("data[].createdAt").description("댓글의 생성 시각"),
                                fieldWithPath("data[].totalReply").description("이 댓글의 답글 개수"),
                                fieldWithPath("data[].writer").description("작성자의 정보(optional)").optional(),
                                fieldWithPath("data[].writer.id").description("작성자의 id"),
                                fieldWithPath("data[].writer.name").description("닉네임"),
                                fieldWithPath("data[].writer.title").description("타이틀"),
                                fieldWithPath("data[].writer.image").description("프로필 사진"),
                                fieldWithPath("data[].replies[]").description("댓글의 답글 목록"),
                                fieldWithPath("data[].replies[].id").description("답글의 id"),
                                fieldWithPath("data[].replies[].parentId").description("답글 부모의 id"),
                                fieldWithPath("data[].replies[].content").description("답글의 내용"),
                                fieldWithPath("data[].replies[].createdAt").description("답글의 생성 시각"),
                                fieldWithPath("data[].replies[].writer.id").description("작성자의 id"),
                                fieldWithPath("data[].replies[].writer.name").description("닉네임"),
                                fieldWithPath("data[].replies[].writer.title").description("타이틀"),
                                fieldWithPath("data[].replies[].writer.image").description("프로필 사진"))
                        .tag("InsightComment")
                        .build()
        )));
    }

    @Test
    @DisplayName("답글 조회 API")
    void get_replies() throws Exception {
        long parentId = 1L;
        long cursor = 0L;
        long limit = 2L;
        String now = LocalDateTime.now().toString();

        ActiveReplyResponse reply1 = ActiveReplyResponse.of(writer1, 2L, parentId, "답글1 내용", now);
        ActiveReplyResponse reply2 = ActiveReplyResponse.of(writer2, 3L, parentId, "답글2 내용", now);

        when(insightCommentQueryApiService.getReplies(any(), any())).thenReturn(List.of(reply1, reply2));

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comments/{parentId}/replies", parentId)
                        .param("cursor", Long.toString(cursor))
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("답글 목록 조회 API 입니다.")
                        .summary("답글 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(parameterWithName("parentId").description("부모 댓글의 id"))
                        .requestParameters(
                                parameterWithName("cursor").description("대상 답글의 id"),
                                parameterWithName("limit").description("가져올 답글의 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("댓글의 id"),
                                fieldWithPath("data[].content").description("댓글의 내용"),
                                fieldWithPath("data[].createdAt").description("댓글의 생성 시각"),
                                fieldWithPath("data[].parentId").description("답글 부모의 id"),
                                fieldWithPath("data[].writer").description("작성자의 정보(optional)").optional(),
                                fieldWithPath("data[].writer.id").description("작성자의 id"),
                                fieldWithPath("data[].writer.name").description("닉네임"),
                                fieldWithPath("data[].writer.title").description("타이틀"),
                                fieldWithPath("data[].writer.image").description("프로필 사진"))
                        .tag("InsightComment")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 댓글 개수 조회 API")
    void count_comment() throws Exception {
        long insightId = 1L;
        InsightCommentCountResponse response = InsightCommentCountResponse.of(1000L);
        when(insightCommentQueryApiService.getCommentCount(anyLong())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/comments/insights/{insightId}/count", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 댓글 개수 조회 API 입니다.")
                        .summary("인사이트 댓글 개수 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.commentCount").description("댓글 총 개수"))
                        .tag("InsightComment")
                        .build()
        )));
    }
}
