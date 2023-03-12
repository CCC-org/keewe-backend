package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keeweapi.dto.insight.ReactionAggregationResponse;
import ccc.keeweapi.service.insight.command.InsightReactionCommandApiService;
import ccc.keeweapi.service.insight.query.InsightReactionQueryApiService;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
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

public class InsightReactionControllerTest extends ApiDocumentationTest {
    @InjectMocks
    InsightReactionController insightReactionController;

    @Mock
    InsightReactionCommandApiService insightReactionCommandApiService;

    @Mock
    InsightReactionQueryApiService insightReactionQueryApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(insightReactionController, provider);
    }

    @Test
    @DisplayName("인사이트 조회수 증가 API")
    void increment_insight_views() throws Exception {
        JSONObject request = new JSONObject();
        request.put("insightId", 0L)
                .put("reactionType", ReactionType.CLAP)
                .put("value", 1L);

        when(insightReactionCommandApiService.react(any())).thenReturn(ReactResponse.of(0L, ReactionType.CLAP, 5L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/reaction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("반응 API 입니다.")
                        .summary("반응 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("insightId").description("인사이트 ID"),
                                fieldWithPath("reactionType").description("반응 종류"),
                                fieldWithPath("value").description("증가 시킬 수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightId").description("인사이트 ID"),
                                fieldWithPath("data.reactionType").description("반응 종류"),
                                fieldWithPath("data.count").description("현재 반응 count"))
                        .tag("InsightReaction")
                        .build()
        )));
    }

    @Test
    @DisplayName("참여중인 챌린지에 기록한 인사이트 반응 합 조회 API")
    void aggregation_reaction_of_challenge() throws Exception {
        JSONObject request = new JSONObject();
        request.put("insightId", 0L)
                .put("reactionType", ReactionType.CLAP)
                .put("value", 1L);

        when(insightReactionQueryApiService.aggregateOfCurrentChallenge()).thenReturn(ReactionAggregationResponse.of(3L, 4L, 5L, 6L, 7L, 8L));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/reaction/challenge-statistics")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("참여중인 챌린지에 기록한 인사이트 반응 합 조회 API입니다.")
                        .summary("참여중인 챌린지에 기록한 인사이트 반응 합 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.clap").description("인사이트 ID"),
                                fieldWithPath("data.sad").description("반응 종류"),
                                fieldWithPath("data.surprise").description("반응 종류"),
                                fieldWithPath("data.heart").description("반응 종류"),
                                fieldWithPath("data.fire").description("반응 종류"),
                                fieldWithPath("data.eyes").description("반응 종류")
                        )
                        .tag("InsightReaction")
                        .build()
        )));
    }
}
