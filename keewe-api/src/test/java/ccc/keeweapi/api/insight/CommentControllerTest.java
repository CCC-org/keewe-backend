package ccc.keeweapi.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.CommentCreateResponse;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ApiDocumentationTest {

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentApiService commentApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(commentController, provider);
    }

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

        ResultActions resultActions = mockMvc.perform(post("/api/v1/comment")
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
}
