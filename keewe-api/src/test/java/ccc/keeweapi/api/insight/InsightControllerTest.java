package ccc.keeweapi.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keeweapi.dto.insight.InsightGetResponse;
import ccc.keeweapi.dto.insight.InsightViewIncrementResponse;
import ccc.keeweapi.dto.insight.ReactionAggregationResponse;
import ccc.keeweapi.service.insight.InsightApiService;
import ccc.keewedomain.persistence.domain.common.Link;
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

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
        String link = "https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/";
        boolean participation = false;
        Long drawerId = 1L;

        JSONObject insightCreateRequest = new JSONObject();
        insightCreateRequest
                .put("contents", contents)
                .put("link", link)
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
                                fieldWithPath("link").description("등록한 링크의 link. 최대 2000자"),
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
    @DisplayName("인사이트 조회 API")
    void get_insight_test() throws Exception {
        Long insightId = 1L;

        when(insightApiService.getInsight(insightId)).thenReturn(InsightGetResponse.of(
                insightId,
                "인사이트 내용입니다. 즐거운 개발 되세요!",
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L)
                )
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 조회 API 입니다.")
                        .summary("인사이트 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.id").description("인사이트 ID"),
                                fieldWithPath("data.contents").description("인사이트 내용"),
                                fieldWithPath("data.link.url").description("인사이트 링크"),
                                fieldWithPath("data.reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data.reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data.reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data.reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data.reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data.reaction.eyes").description("인사이트 눈 반응 수"))
                        .tag("Insight")
                        .build()
        )));
    }


    @Test
    @DisplayName("인사이트 조회수 증가 API")
    void increment_insight_views() throws Exception {

        when(insightApiService.incrementViewCount(anyLong())).thenReturn(InsightViewIncrementResponse.of(123L));

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
                                fieldWithPath("data.viewCount").description("조회수"))
                        .tag("Insight")
                        .build()
        )));
    }
}
