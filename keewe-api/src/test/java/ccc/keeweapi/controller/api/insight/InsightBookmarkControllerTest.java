package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.BookmarkToggleResponse;
import ccc.keeweapi.service.insight.command.InsightCommandApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

public class InsightBookmarkControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightBookmarkController insightBookmarkController;

    @Mock
    InsightCommandApiService insightCommandApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(insightBookmarkController, provider);
    }

    @Test
    @DisplayName("인사이트 북마크 토글 API")
    void insight_bookmark() throws Exception {

        when(insightCommandApiService.toggleInsightBookmark(anyLong())).thenReturn(
                BookmarkToggleResponse.of(true)
        );

        ResultActions resultActions = mockMvc.perform(post("/api/v1/insight/bookmark/{insightId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 북마크 등록/제거 (토글) API 입니다.")
                        .summary("인사이트 북마크 토글 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.bookmark").description("북마크 여부 (토글이므로 false, true를 왔다갔다 함)"))
                        .tag("InsightBookmark")
                        .build()
        )));
    }
}
