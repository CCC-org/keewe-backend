package ccc.keeweapi.controller.api.challenge;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.challenge.ChallengeCreateResponse;
import ccc.keeweapi.dto.challenge.ChallengeDetailResponse;
import ccc.keeweapi.dto.challenge.ChallengeInsightNumberResponse;
import ccc.keeweapi.dto.challenge.ChallengeStatisticsResponse;
import ccc.keeweapi.dto.challenge.OpenedChallengeResponse;
import ccc.keeweapi.dto.challenge.ParticipatingChallengeDetailResponse;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeQueryApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDate;
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
    @DisplayName("챌린지 상세 조회 API")
    void get_challenge_detail() throws Exception {
        long challengeId = 1L;
        ChallengeDetailResponse response = ChallengeDetailResponse.of("챌린지 이름", "챌린지 설명~~", 1000L, "2019-01-01");

        when(challengeApiService.getChallengeDetail(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/{challengeId}/detail", challengeId)
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
                                fieldWithPath("data.challengeName").description("챌린지의 이름"),
                                fieldWithPath("data.challengeIntroduction").description("챌린지 설명"),
                                fieldWithPath("data.insightCount").description("챌린지에 기록한 인사이트 수"),
                                fieldWithPath("data.createdAt").description("챌린지의 시작일자 yyyy-mm-dd")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("참가중인 챌린지 상세 조회 API")
    void get_participating_challenge_detail() throws Exception {
        ParticipatingChallengeDetailResponse response = ParticipatingChallengeDetailResponse.of("챌린지 이름", "개발", "챌린지 소개", "2023-03-05");

        when(challengeApiService.getMyChallengeDetail())
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/my/detail")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("참가중인 챌린지 상세 조회 API 입니다.")
                        .summary("참가중인 챌린지 상세 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.challengeName").description("챌린지의 이름"),
                                fieldWithPath("data.challengeCategory").description("챌린지의 카테고리"),
                                fieldWithPath("data.challengeIntroduction").description("챌린지 설명"),
                                fieldWithPath("data.createdAt").description("챌린지의 시작일자 yyyy-mm-dd")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("진행 중인 전체 챌린지 페이지네이션 조회")
    void paginate_challenges() throws Exception {
        List<OpenedChallengeResponse> response = List.of(
                OpenedChallengeResponse.of(3L, "카테고리", "챌린지설명", "챌린지명", 127L)
        );

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
                        .requestParameters(
                                parameterWithName("cursor").description("마지막으로 받은 챌린지 ID, 첫 요청 시 비우고 요청"),
                                parameterWithName("limit").description("가져올 챌린지 개수")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터, 오류 시 null"),
                                fieldWithPath("data[].challengeId").description("챌린지의 ID"),
                                fieldWithPath("data[].challengeIntroduction").description("챌린지의 설명"),
                                fieldWithPath("data[].challengeName").description("챌린지의 이름"),
                                fieldWithPath("data[].challengeCategory").description("챌린지 카테고리"),
                                fieldWithPath("data[].insightCount").description("인사이트 수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("참여중인 챌린지 통계 조회 API")
    void aggregation_reaction_of_challenge() throws Exception {
        when(challengeQueryApiService.aggregateChallengeStatistics()).thenReturn(ChallengeStatisticsResponse.of(4L, 5L, 6L, 7L, 8L));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/statistics")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("참여중인 챌린지 통계 조회 API입니다.")
                        .summary("참여중인 챌린지 통계 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.bookmarkCount").description("북마크 개수"),
                                fieldWithPath("data.commentCount").description("댓글 개수"),
                                fieldWithPath("data.reactionCount").description("리액션 개수"),
                                fieldWithPath("data.shareCount").description("공유하기 횟수"),
                                fieldWithPath("data.viewCount").description("조회수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 통계 조회 API")
    void aggregation_challenge() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/{challengeId}/statistics", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("특정 챌린지 통계 조회 API입니다.")
                        .summary("특정 챌린지 통계 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.bookmarkCount").description("북마크 개수"),
                                fieldWithPath("data.commentCount").description("댓글 개수"),
                                fieldWithPath("data.reactionCount").description("리액션 개수"),
                                fieldWithPath("data.shareCount").description("공유하기 횟수"),
                                fieldWithPath("data.viewCount").description("조회수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }

    @Test
    @DisplayName("나의 챌린지 인사이트 수 조회 API")
    void get_challenge_insight_count() throws Exception {
        ChallengeInsightNumberResponse response = ChallengeInsightNumberResponse.of(100);

        when(challengeApiService.countInsightOfChallenge(any())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/my/insight/count")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("나의 챌린지 인사이트 수 조회 API 입니다.")
                        .summary("나의 챌린지 인사이트 수 조회 API")
                        .requestParameters(
                                parameterWithName("writerId").optional().description("필터링할 작성자 ID, 없을 시 전체 조회")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightNumber").description("챌린지의 전체 인사이트 수")
                        )
                        .tag("Challenge")
                        .build()
        )));
    }
}
