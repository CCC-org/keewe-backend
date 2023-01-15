package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.snippet.Attributes.key;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.service.insight.ReportApiService;
import ccc.keewedomain.persistence.domain.insight.enums.ReportType;
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

public class ReportControllerTest extends ApiDocumentationTest {

    @InjectMocks
    ReportController reportController;

    @Mock
    ReportApiService reportApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(reportController, provider);
    }

    @Test
    @DisplayName("인사이트 신고 API")
    void insight_report_post() throws Exception {
        // given
        doNothing().when(reportApiService).reportInsight(any());


        JSONObject reportRequest = new JSONObject()
                .put("insightId", 1L)
                .put("reportType", "SPAM")
                .put("reason", "그냥 보기싫은 글");
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/report")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(reportRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 신고 API 입니다.")
                        .summary("인사이트 신고 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("insightId").description("인사이트ID"),
                                fieldWithPath("reportType").description("신고 카테고리").type("ENUM")
                                        .attributes(key("enumValues").value(List.of(ReportType.values()))),
                                fieldWithPath("reason").description("인사이트 신고이유")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("응답 데이터"))
                        .tag("Report")
                        .build()
        )));

    }
}
