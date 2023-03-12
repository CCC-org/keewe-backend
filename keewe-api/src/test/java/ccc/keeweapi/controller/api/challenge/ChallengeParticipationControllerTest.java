package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import ccc.keeweapi.service.challenge.query.ChallengeParticipationQueryApiService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeParticipationControllerTest extends ApiDocumentationTest {

    @InjectMocks
    ChallengeParticipationController challengeParticipationController;

    @Mock
    ChallengeApiService challengeApiService;

    @Mock
    ChallengeParticipationQueryApiService challengeParticipationQueryApiService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(challengeParticipationController, provider);
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
                        .tag("ChallengeParticipation")
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
                        .tag("ChallengeParticipation")
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
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("홈 나의 챌린지 참가 현황 조회 API")
    void home_my_week_progress() throws Exception {
        Long challengeId = 1L;
        Long remain = 2L;
        String challengeName = "챌린지 이름";
        LocalDate startDate = LocalDate.now();
        List<DayProgressResponse> dayProgresses = List.of(
                DayProgressResponse.of(true),
                DayProgressResponse.of(true),
                DayProgressResponse.of(false),
                DayProgressResponse.of(true),
                DayProgressResponse.of(false),
                DayProgressResponse.of(true),
                DayProgressResponse.of(false)
        );

        when(challengeApiService.getWeekProgress())
                .thenReturn(WeekProgressResponse.of(challengeId, remain, challengeName, startDate.toString(), dayProgresses));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/participation/my-week-progress")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("홈 나의 챌린지 참가 현황 조회 API 입니다.")
                        .summary("홈 나의 챌린지 참가 현황 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("참가중이지 않은 경우 null"),
                                fieldWithPath("data.challengeId").description("참가중인 챌린지의 ID"),
                                fieldWithPath("data.challengeName").description("챌린지의 이름"),
                                fieldWithPath("data.remain").description("이번 주의 남은 인사이트의 수"),
                                fieldWithPath("data.startDate").description("이번 주의 챌린지 시작일"),
                                fieldWithPath("data.dayProgresses[].check").description("해당 날짜에 인사이트를 작성했는지 true or false")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("홈 나의 챌린지 조회 API")
    void home_my_challenge() throws Exception {
        long challengeId = 1L;
        String name = "챌린지 이름";
        Long participatingUser = 9999L;
        String interest = "프론트";
        String startDate = "2023-02-04";
        ParticipatingChallengeResponse response = ParticipatingChallengeResponse.of(challengeId, name, participatingUser, interest, startDate);

        when(challengeApiService.getParticipatingChallenege()).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/participating")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("홈 나의 챌린지 조회 API 입니다.")
                        .summary("홈 나의 챌린지 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("참가중이지 않은 경우 null"),
                                fieldWithPath("data.challengeId").description("참가중인 챌린지의 ID"),
                                fieldWithPath("data.name").description("참가중인 챌린지의 이름"),
                                fieldWithPath("data.participatingUserNumber").description("도전(참가)중인 유저의 수"),
                                fieldWithPath("data.interest").description("관심사"),
                                fieldWithPath("data.startDate").description("챌린지에 참가한 날짜")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 상세 함께 기록 조회 API")
    void get_together() throws Exception {
        List<FriendResponse> response = List.of(
                FriendResponse.of("닉네임1", "이미지 URL1", 1L, 4L, true),
                FriendResponse.of("닉네임2", "이미지 URL2", 2L, 4L, false),
                FriendResponse.of("닉네임3", "이미지 URL3", 3L, 4L, false)
        );

        when(challengeApiService.paginateFriends(anyLong(), any())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/{challengeId}/friends", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("함께 기록(친구) 조회 API 입니다.")
                        .summary("함께 기록(친구) 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .requestParameters(
                                parameterWithName("page").description("페이지 번호. 미 입력시 기본 0").optional(),
                                parameterWithName("size").description("페이지 당 결과 개수. 미 입력시 기본 10").optional()
                        )
                        .pathParameters(parameterWithName("challengeId").description("챌린지의 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("없는 경우 비어 있음. 최대 5개 조회"),
                                fieldWithPath("data[].nickname").description("참가자의 닉네임"),
                                fieldWithPath("data[].imageURL").description("참가자의 프로필 이미지 URL"),
                                fieldWithPath("data[].currentRecord").description("현재 기록한 개수"),
                                fieldWithPath("data[].goalRecord").description("전체 기록해야 하는 개수"),
                                fieldWithPath("data[].following").description("팔로우 여부")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("챌린지 참가중인 인원 조회 API")
    void get_challenger_count() throws Exception {
        ChallengerCountResponse response = ChallengerCountResponse.of(1000L);

        when(challengeApiService.getChallengerCount(anyLong())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/{challengeId}/challengers/count", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 참가중인 인원 조회 API 입니다.")
                        .summary("챌린지 참가중인 인원 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .pathParameters(parameterWithName("challengeId").description("챌린지의 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.challengerCount").description("참가자의 닉네임")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("완료된 챌린지 지정한 개수 조회")
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
                        .description("완료된 챌린지 지정한 개수 만큼 조회 API 입니다.")
                        .summary("완료된 챌린지 조회 지정한 개수 만큼 조회 API")
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
    @DisplayName("종료된 챌린지 페이지네이션 조회")
    void get_completed_challenges() throws Exception {
        ChallengerCountResponse response = ChallengerCountResponse.of(1000L);

        List<FinishedChallengeResponse> responses = List.of(
                FinishedChallengeResponse.of(1L, 3L, "개발", "개발챌린지", "2023-03-05", "2023-03-12")
        );

        when(challengeParticipationQueryApiService.paginateFinished(any())).thenReturn(responses);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/finished")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("cursor", "100")
                        .param("limit", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("종료된 챌린지 페이지네이션 조회 API 입니다.")
                        .summary("종료된 챌린지 페이지네이션 조회 API")
                        .requestParameters(
                                parameterWithName("cursor").description("마지막으로 받은 챌린지 참가 현황(challengeParticipationId) ID, 첫 요청 시 비우고 요청"),
                                parameterWithName("limit").description("가져올 챌린지 개수")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터, 오류 시 null"),
                                fieldWithPath("data[].challengeParticipationId").description("챌린지 참가 현황 ID"),
                                fieldWithPath("data[].challengeId").description("챌린지의 ID"),
                                fieldWithPath("data[].challengeName").description("챌린지의 이름"),
                                fieldWithPath("data[].challengeCategory").description("챌린지 카테고리"),
                                fieldWithPath("data[].startDate").description("챌린지 생성일"),
                                fieldWithPath("data[].endDate").description("챌린지 종료일")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }

    @Test
    @DisplayName("종료된 챌린지 개수 조회")
    void count_completed_challenges() throws Exception {
        FinishedChallengeCountResponse response = FinishedChallengeCountResponse.of(14L);

        when(challengeParticipationQueryApiService.countFinished()).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/challenge/finished/count")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("종료된 챌린지 개수 조회 API 입니다.")
                        .summary("종료된 챌린지 개수 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터, 오류 시 null"),
                                fieldWithPath("data.count").description("종료된 챌린지 개수")
                        )
                        .tag("ChallengeParticipation")
                        .build()
        )));
    }
}
