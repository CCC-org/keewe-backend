package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeQueryApiService;
import ccc.keewedomain.persistence.domain.common.Interest;
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
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    ChallengeQueryApiService challengeQueryApiService;

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
    @DisplayName("진행 중인 전체 챌린지 일부 조회")
    void get_progress_recent_challenge() throws Exception {
        List<ChallengeInfoResponse> response = List.of(
                ChallengeInfoResponse.of(3L, Interest.of("카테고리"), "챌린지명", "챌린지설명", 5L)
        );

        when(challengeApiService.getSpecifiedNumberOfChallenge(anyInt()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/specified-size")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("진행중인 최근 챌린지 현황 중 지정한 개수 만큼 조회하는 API 입니다.")
                        .summary("진행중인 최근 챌린지 현황 중 지정한 개수 만큼 조회하는 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터, 오류 시 null"),
                                fieldWithPath("data[].challengeId").description("챌린지의 ID"),
                                fieldWithPath("data[].challengeName").description("챌린지의 이름"),
                                fieldWithPath("data[].challengeCategory").description("챌린지 카테고리"),
                                fieldWithPath("data[].challengeIntroduction").description("챌린지 설명"),
                                fieldWithPath("data[].insightCount").description("챌린지에 기록한 인사이트 수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("종료된 챌린지 조회")
    void get_challenge_history() throws Exception {
        List<ChallengeHistoryResponse> historyResponses = List.of(
                ChallengeHistoryResponse.of(1L, "개발", "챌린지1", "2023-01-01", "2023-01-13"),
                ChallengeHistoryResponse.of(2L, "개발", "챌린지2", "2023-02-01", "2023-02-13")
        );
        ChallengeHistoryListResponse response = ChallengeHistoryListResponse.of(
                2L,
                historyResponses
        );

        when(challengeApiService.getHistoryOfChallenge(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/history")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("종료된 챌린지 지정한 개수 만큼 조회 API 입니다.")
                        .summary("종료된 챌린지 조회 지정한 개수 만큼 조회 API")
                        .requestParameters(
                                parameterWithName("size").description("조회할 챌린지 기록 개수. 미설정 시 전체 조회").optional()
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.historyNumber").description("전체 종료된 챌린지 개수, 최소값 1(size와 관련 없음)"),
                                fieldWithPath("data.challengeHistories[].challengeId").description("챌린지의 ID"),
                                fieldWithPath("data.challengeHistories[].challengeCategory").description("챌린지의 카테고리(관심사)"),
                                fieldWithPath("data.challengeHistories[].challengeName").description("챌린지 이름"),
                                fieldWithPath("data.challengeHistories[].startDate").description("챌린지 참가일"),
                                fieldWithPath("data.challengeHistories[].endDate").description("챌린지 종료일")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 상세 조회 API")
    void get_challenge_detail() throws Exception {
        long challengeId = 1L;
        ChallengeDetailResponse response = ChallengeDetailResponse.of(challengeId, "챌린지 이름", "챌린지 설명~~", 1000L);

        when(challengeApiService.getChallengeDetail(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/detail/{challengeId}", challengeId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 상세 조회 API 입니다.")
                        .summary("챌린지 상세 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.challengeId").description("챌린지의 ID"),
                                fieldWithPath("data.challengeName").description("챌린지의 이름"),
                                fieldWithPath("data.challengeIntroduction").description("챌린지 설명"),
                                fieldWithPath("data.insightCount").description("챌린지에 기록한 인사이트 수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("진행 중인 전체 챌린지 페이지네이션 조회")
    void paginate_challenges() throws Exception {
        List<OpenedChallengeResponse> response = List.of(OpenedChallengeResponse.of(3L, "카테고리", "챌린지명", "2023-02-25"));

        when(challengeQueryApiService.paginate(any()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("cursor", "100")
                        .param("limit", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("진행중인 최근 챌린지 현황을 페이지네이션 조회하는 API 입니다.")
                        .summary("진행중인 최근 챌린지 현황을 페이지네이션 조회하는 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터, 오류 시 null"),
                                fieldWithPath("data[].challengeId").description("챌린지의 ID"),
                                fieldWithPath("data[].challengeName").description("챌린지의 이름"),
                                fieldWithPath("data[].challengeCategory").description("챌린지 카테고리"),
                                fieldWithPath("data[].startDate").description("챌린지 생성일")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }
}
