package ccc.keeweapi.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.service.insight.DrawerApiService;
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

public class DrawerControllerTest extends ApiDocumentationTest {

    @InjectMocks
    DrawerController drawerController;

    @Mock
    DrawerApiService drawerApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(drawerController, provider);
    }

    @Test
    @DisplayName("서랍 등록 API")
    void create_insight_test() throws Exception {
        String drawerName = "개발";

        JSONObject drawerCreateRequest = new JSONObject();
        drawerCreateRequest
                .put("name", drawerName);

        when(drawerApiService.create(any())).thenReturn(DrawerCreateResponse.of(1L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/drawer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(drawerCreateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("서랍 생성 API 입니다.")
                        .summary("서랍 생성 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("name").description("서랍의 이름. 15자 이내"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.drawerId").description("생성된 서랍의 ID"))
                        .tag("Insight")
                        .build()
        )));
    }
}