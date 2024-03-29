package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.response.ProfileVisitFromInsightCountResponse;
import ccc.keeweapi.dto.user.FollowFromInsightCountResponse;
import ccc.keeweapi.service.insight.query.InsightStatisticsQueryApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InsightStatisticsControllerTest extends ApiDocumentationTest {
    @InjectMocks
    InsightStatisticsController insightStatisticsController;

    @Mock
    InsightStatisticsQueryApiService insightStatisticsQueryApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(insightStatisticsController, provider);
    }

    @Test
    @DisplayName("인사이트를 통한 팔로우 수 조회 API")
    void count_follow_from_insight() throws Exception {
        Long insightId = 1L;
        FollowFromInsightCountResponse response = FollowFromInsightCountResponse.of(1000L);
        when(insightStatisticsQueryApiService.countFollowFromInsight(insightId)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}/statistics/follow", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트를 통한 팔로우 수 API 입니다.")
                        .summary("인사이트를 통한 팔로우 수 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(
                                parameterWithName("insightId").description("인사이트 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.count").description("인사이트를 통한 팔로우 count"))
                        .tag("InsightStatistics")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트를 통한 프로필 방문 횟수 조회 API")
    void get_profile_visit_from_insight_count() throws Exception {
        Long insightId = 1L;
        ProfileVisitFromInsightCountResponse response = ProfileVisitFromInsightCountResponse.of(100L);

        when(insightStatisticsQueryApiService.countProfileVisitFromInsight(anyLong())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}/statistics/profile-visit", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트를 통한 프로필 방문 횟수 조회 API 입니다.")
                        .summary("인사이트를 통한 프로필 방문 횟수 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .pathParameters(
                                parameterWithName("insightId").description("인사이트 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.count").description("인사이트를 통한 프로필 방문 횟수 합계"))
                        .tag("InsightStatistics")
                        .build()
        )));
    }

}
