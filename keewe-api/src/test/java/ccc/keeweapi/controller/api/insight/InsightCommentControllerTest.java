package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.service.insight.CommentApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InsightCommentControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightCommentController insightCommentController;

    @Mock
    CommentApiService commentApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(insightCommentController, provider);
    }

    private CommentWriterResponse writer1 = CommentWriterResponse.of(1L, "유승훈", "타이틀1", "www.api-keewe.com/images");
    private CommentWriterResponse writer2 = CommentWriterResponse.of(2L, "최지훈", "타이틀2", "www.api-keewe.com/images");

    @Test
    @DisplayName("댓글 등록 API")
    void create_comment() throws Exception {

        String content = "취업은 하반기에 끝내자";
        Long insightId = 1L;
        Long parentId = 1L;

        Long commentId = 2L;

        JSONObject commentCreateRequest = new JSONObject();
        commentCreateRequest
                .put("content", content)
                .put("insightId", insightId)
                .put("parentId", parentId);

        when(commentApiService.create(any())).thenReturn(CommentCreateResponse.of(commentId));

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
                                fieldWithPath("data.commentId").description("생성된 댓글(답글) ID"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("대표 댓글 조회 API")
    void get_representative_comment() throws Exception {
        Long insightId = 1L;
        Long totalReply = 2L;
        String now = LocalDateTime.now().toString();

        ReplyResponse reply1 = ReplyResponse.of(writer1, 2L, 1L, "답글1 내용", now);
        ReplyResponse reply2 = ReplyResponse.of(writer1, 3L, 1L, "답글2 내용", now);
        CommentResponse commentResponse = CommentResponse.of(1L, writer1, "댓글의 내용", now, List.of(reply1, reply2), totalReply);
        RepresentativeCommentResponse response = RepresentativeCommentResponse.of(10L, List.of(commentResponse));

        when(commentApiService.getRepresentativeComments(insightId)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comments/representative/insights/{insightId}", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("대표 댓글 조회 API 입니다.")
                        .summary("대표 댓글 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(parameterWithName("insightId").description("대상 인사이트의 id"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.total").description("인사이트의 총 댓글 개수"),
                                fieldWithPath("data.comments[].id").description("댓글의 id"),
                                fieldWithPath("data.comments[].content").description("댓글의 내용"),
                                fieldWithPath("data.comments[].createdAt").description("댓글의 생성 시각"),
                                fieldWithPath("data.comments[].totalReply").description("이 댓글의 답글 개수"),
                                fieldWithPath("data.comments[].writer.id").description("작성자의 id"),
                                fieldWithPath("data.comments[].writer.name").description("닉네임"),
                                fieldWithPath("data.comments[].writer.title").description("타이틀"),
                                fieldWithPath("data.comments[].writer.image").description("프로필 사진"),
                                fieldWithPath("data.comments[].replies[]").description("댓글의 답글 목록"),
                                fieldWithPath("data.comments[].replies[].id").description("답글의 id"),
                                fieldWithPath("data.comments[].replies[].parentId").description("답글 부모의 id"),
                                fieldWithPath("data.comments[].replies[].content").description("답글의 내용"),
                                fieldWithPath("data.comments[].replies[].createdAt").description("답글의 생성 시각"),
                                fieldWithPath("data.comments[].replies[].writer.id").description("작성자의 id"),
                                fieldWithPath("data.comments[].replies[].writer.name").description("닉네임"),
                                fieldWithPath("data.comments[].replies[].writer.title").description("타이틀"),
                                fieldWithPath("data.comments[].replies[].writer.image").description("프로필 사진"))
                        .tag("Insight")
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

        ReplyResponse reply1 = ReplyResponse.of(writer2, 3L, 1L, "답글1 내용", now);
        ReplyResponse reply2 = ReplyResponse.of(writer2, 4L, 1L, "답글2 내용", now);
        CommentResponse comment1 = CommentResponse.of(1L, writer1, "댓글의 내용1", now, List.of(reply1), totalReply);
        CommentResponse comment2 = CommentResponse.of(2L, writer1, "댓글의 내용2", now, List.of(reply2), totalReply);

        when(commentApiService.getCommentsWithFirstReply(any(), any())).thenReturn(List.of(comment1, comment2));

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
                        .tag("Insight")
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

        ReplyResponse reply1 = ReplyResponse.of(writer1, 2L, parentId, "답글1 내용", now);
        ReplyResponse reply2 = ReplyResponse.of(writer2, 3L, parentId, "답글2 내용", now);

        when(commentApiService.getReplies(any(), any())).thenReturn(List.of(reply1, reply2));

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
                                fieldWithPath("data[].writer.id").description("작성자의 id"),
                                fieldWithPath("data[].writer.name").description("닉네임"),
                                fieldWithPath("data[].writer.title").description("타이틀"),
                                fieldWithPath("data[].writer.image").description("프로필 사진"))
                        .tag("Insight")
                        .build()
        )));
    }
}
