package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.response.DrawerCreateResponse;
import ccc.keeweapi.dto.insight.request.DrawerResponse;
import ccc.keeweapi.service.insight.command.InsightDrawerCommandApiService;
import ccc.keeweapi.service.insight.query.InsightDrawerQueryApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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

public class InsightDrawerControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightDrawerController insightDrawerController;

    @Mock
    InsightDrawerCommandApiService insightDrawerCommandApiService;

    @Mock
    InsightDrawerQueryApiService insightDrawerQueryApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(insightDrawerController, provider);
    }

    @Test
    @DisplayName("폴더 등록 API")
    void create_drawer_test() throws Exception {
        String drawerName = "개발";

        JSONObject drawerCreateRequest = new JSONObject();
        drawerCreateRequest
                .put("name", drawerName);

        when(insightDrawerCommandApiService.create(any())).thenReturn(DrawerCreateResponse.of(1L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/drawer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(drawerCreateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("폴더 생성 API 입니다.")
                        .summary("폴더 생성 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("name").description("폴더의 이름. 15자 이내"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.drawerId").description("생성된 폴더의 ID"))
                        .tag("InsightDrawer")
                        .build()
        )));
    }

    @Test
    @DisplayName("나의 폴더 조회 API")
    void get_my_drawers_test() throws Exception {
        List<DrawerResponse> responses = List.of(
                DrawerResponse.of(1L, "개발"),
                DrawerResponse.of(2L, "공부"),
                DrawerResponse.of(3L, "디자인")
        );

        when(insightDrawerQueryApiService.getMyDrawers()).thenReturn(responses);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/drawer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("나의 폴더 조회 API 입니다.")
                        .summary("나의 폴더 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.[].id").description("폴더의 ID"),
                                fieldWithPath("data.[].name").description("폴더의 이름"))
                        .tag("InsightDrawer")
                        .build()
        )));
    }

    @Test
    @DisplayName("유저의 폴더 조회 API")
    void get_drawers_test() throws Exception {

        Long userId = 1L;

        List<DrawerResponse> responses = List.of(
                DrawerResponse.of(1L, "개발"),
                DrawerResponse.of(2L, "공부"),
                DrawerResponse.of(3L, "디자인")
        );

        when(insightDrawerQueryApiService.getDrawers(userId)).thenReturn(responses);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/drawer/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("유저의 폴더 조회 API 입니다.")
                        .summary("유저의 폴더 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.[].id").description("폴더의 ID"),
                                fieldWithPath("data.[].name").description("폴더의 이름"))
                        .tag("InsightDrawer")
                        .build()
        )));
    }

    @Test
    @DisplayName("폴더 수정 API")
    void update_drawer() throws Exception {
        JSONObject request = new JSONObject()
                .put("name", "새로운 폴더 이름");

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/drawer/{drawerId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(request.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("폴더 수정 API 입니다.")
                        .summary("폴더 수정 API")
                        .pathParameters(
                                parameterWithName("drawerId").description("수정할 폴더의 ID")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("비어 있음(null)")
                        )
                        .tag("InsightDrawer")
                        .build()
        )));
    }

    @Test
    @DisplayName("폴더 삭제 API")
    void delete_drawer() throws Exception {

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/drawer/{drawerId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("폴더 삭제 API 입니다.")
                        .summary("폴더 삭제 API")
                        .pathParameters(
                                parameterWithName("drawerId").description("삭제할 폴더의 ID")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("비어 있음(null)")
                        )
                        .tag("InsightDrawer")
                        .build()
        )));
    }
}
