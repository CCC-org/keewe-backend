package ccc.keeweapi.api.challenge;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.challenge.ChallengeCreateResponse;
import ccc.keeweapi.dto.challenge.ChallengeParticipationResponse;
import ccc.keeweapi.dto.challenge.InsightProgressResponse;
import ccc.keeweapi.dto.challenge.ParticipationCheckResponse;
import ccc.keeweapi.service.challenge.ChallengeApiService;
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

import java.time.LocalDate;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeControllerTest extends ApiDocumentationTest {

    @InjectMocks
    ChallengeController challengeController;

    @Mock
    ChallengeApiService challengeApiService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(challengeController, provider);
    }

    @Test
    @DisplayName("챌린지 등록 API")
    void create_challenge() throws Exception {
        Long challengeId = 1L;
        String interest = "개발";
        String name = "하루 한 문제 풀기";
        String introduction = "알고리즘 하루에 하나씩 풀기";
        int insightPerWeek = 5;
        int duration = 4;
        String myTopic = "하기 싫어요";

        JSONObject createRequest = new JSONObject();
        JSONObject participateRequest = new JSONObject();
        participateRequest
                .put("insightPerWeek", insightPerWeek)
                .put("duration", duration)
                .put("myTopic", myTopic);

        createRequest
                .put("interest", interest)
                .put("name", name)
                .put("introduction", introduction)
                .put("participate", participateRequest);

        when(challengeApiService.createChallenge(any())).thenReturn(
                ChallengeCreateResponse.of(challengeId, name, myTopic, insightPerWeek, duration, LocalDate.of(2022, 9, 18)));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/challenge")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(createRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 생성 API 입니다..")
                        .summary("챌린지 생성 API 입니다.")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("interest").description("관심사(한글, 영어만 8글자 이하. 공백 X"),
                                fieldWithPath("name").description("챌린지 이름"),
                                fieldWithPath("introduction").description("챌린지 소개").optional(),
                                fieldWithPath("participate.insightPerWeek").description("주마다 올릴 인사이트 개수 1~7"),
                                fieldWithPath("participate.duration").description("참가 기간 2~8, 단위: 주"),
                                fieldWithPath("participate.myTopic").description("나만의 주제 150자 이하"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.challengeId").description("생성된 챌린지의 ID"),
                                fieldWithPath("data.challengeName").description("생성된 챌린지 이름"),
                                fieldWithPath("data.myTopic").description("나만의 주제"),
                                fieldWithPath("data.insightPerWeek").description("주마다 올릴 인사이트 개수"),
                                fieldWithPath("data.duration").description("참가 기간 단위: 주"),
                                fieldWithPath("data.endDate").description("챌린지 참가 종료일 yyyy-mm-dd"))
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 참여")
    void participate_challenge() throws Exception {
        long challengeId = 0L;
        int insightPerWeek = 5;
        int duration = 4;
        String myTopic = "하기 싫어요";

        JSONObject participateRequest = new JSONObject();
        participateRequest
                .put("challengeId", challengeId)
                .put("insightPerWeek", insightPerWeek)
                .put("duration", duration)
                .put("myTopic", myTopic);

        when(challengeApiService.participate(any())).thenReturn(
                ChallengeParticipationResponse.of(myTopic, insightPerWeek, duration, LocalDate.of(2022, 9, 18)));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/challenge/participation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(participateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 참여 API 입니다..")
                        .summary("챌린지 참여 API 입니다.")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("challengeId").description("참여할 챌린지 ID"),
                                fieldWithPath("insightPerWeek").description("주마다 올릴 인사이트 개수 1~7"),
                                fieldWithPath("duration").description("참가 기간 2~8, 단위: 주"),
                                fieldWithPath("myTopic").description("나만의 주제 150자 이하"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.myTopic").description("나만의 주제"),
                                fieldWithPath("data.insightPerWeek").description("주마다 올릴 인사이트 개수"),
                                fieldWithPath("data.duration").description("참가 기간 단위: 주"),
                                fieldWithPath("data.endDate").description("챌린지 참가 종료일 yyyy-mm-dd"))
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 참여 여부 확인 API")
    void check_participation() throws Exception {
        when(challengeApiService.checkParticipation()).thenReturn(
                ParticipationCheckResponse.of(true)
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/participation/check")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 참여 여부 API 입니다..")
                        .summary("챌린지 참여 여부 API 입니다.")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.participation").description("챌린지 참여 여부")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 생성 시 챌린지 현황 조회 API")
    void insight_create_progress() throws Exception {
        String name = "챌린지 이름";
        Long current = 4L;
        Long total = 16L;
        when(challengeApiService.getMyParticipationProgress())
                .thenReturn(InsightProgressResponse.of(name, current, total));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/participation/progress/insight")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 생성 시 챌린지 현황 조회 API 입니다.")
                        .summary("인사이트 생성 시 챌린지 현황 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("참가중이지 않은 경우 null"),
                                fieldWithPath("data.name").description("참가중인 챌린지의 이름"),
                                fieldWithPath("data.current").description("지금까지 기록한 인사이트의 수"),
                                fieldWithPath("data.total").description("총 기록해야 하는 인사이트의 수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }
}
