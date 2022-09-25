package ccc.keeweapi.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keeweapi.service.insight.InsightApiService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InsightControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightController insightController;

    @Mock
    InsightApiService insightApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(insightController, provider);
    }

    @Test
    @DisplayName("인사이트 등록 API")
    void create_insight_test() throws Exception {
        String contents = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용";
        String url = "https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/";
        boolean participation = false;
        Long drawerId = 1L;

        JSONObject insightCreateRequest = new JSONObject();
        insightCreateRequest
                .put("contents", contents)
                .put("url", url)
                .put("participation", participation)
                .put("drawerId", drawerId);

        when(insightApiService.create(any())).thenReturn(InsightCreateResponse.of(1L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/insight")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(insightCreateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 생성 API 입니다.")
                        .summary("인사이트 생성 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("contents").description("인사이트의 내용. 최대 300자, 문자 제한 없음"),
                                fieldWithPath("url").description("등록한 링크의 url. 최대 2000자"),
                                fieldWithPath("participation").description("현재 진행중인 챌린지에 참가할 지 여부"),
                                fieldWithPath("drawerId").description("서랍의 ID. 선택").optional())
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightId").description("생성된 인사이트의 ID"))
                        .tag("Insight")
                        .build()
        )));
    }


    @Test
    @DisplayName("인사이트 조회수 증가 API")
    void increment_insight_views() throws Exception {

        doNothing().when(insightApiService).incrementViewCount(anyLong());

        ResultActions resultActions = mockMvc.perform(post("/api/v1/insight/view/{insightId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 조회수 증가 API 입니다.")
                        .summary("인사이트 조회수 증가 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터 없음"))
                        .tag("Insight")
                        .build()
        )));
    }
}
