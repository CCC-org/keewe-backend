package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.domain.common.Interest;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.json.JSONArray;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends ApiDocumentationTest {

    @InjectMocks
    ProfileController profileController;

    @Mock
    ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(profileController, provider);
    }

    @Test
    @DisplayName("온보딩")
    void onboard_test() throws Exception {
        // given
        Long userId = 1L;
        String nickname = "치훈지훈승훈";
        List<String> interests = List.of("개발", "밥먹기");
        JSONArray jsonArray = new JSONArray(interests);

        JSONObject onboardRequest = new JSONObject();
        onboardRequest
                .put("nickname", nickname)
                .put("interests", jsonArray);
        System.out.println("onboardRequest.toString() = " + onboardRequest.toString());
        when(profileApiService.onboard(any())).thenReturn(
                OnboardResponse.of(userId, nickname, interests.stream().map(Interest::of).collect(Collectors.toList())));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(onboardRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("온보딩 API 입니다.")
                        .summary("온보딩 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("nickname").description("등록할 닉네임. 12자 이내."),
                                fieldWithPath("interests").description("관심사 리스트"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.userId").description("유저의 ID"),
                                fieldWithPath("data.nickname").description("등록된 닉네임"),
                                fieldWithPath("data.interests[].name").description("등록한 관심사 이름"))
                        .tag("Profile")
                        .build()
        )));
    }


    @Test
    @DisplayName("팔로잉")
    void following_test() throws Exception {
        when(profileApiService.toggleFollowership(anyLong())).thenReturn(
                FollowToggleResponse.of(true)
        );

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/profile/follow/{userId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("팔로잉 API 입니다.")
                        .summary("유저 팔로잉 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.following").description("팔로잉 토글 결과 (true: 팔로잉 상태, false: 언팔로잉 상태)"))
                        .tag("Profile")
                        .build()
        )));
    }

    @Test
    @DisplayName("마이페이지 프로필 조회 테스트")
    void my_page_profile() throws Exception {
        when(profileApiService.getMyPageProfile(anyLong())).thenReturn(
                ProfileMyPageResponse.of(
                        "닉네임",
                        "www.api-keewe.com/images/128398681",
                        "대표 타이틀",
                        "안녕하세요. 키위입니다.",
                        List.of("개발", "백엔드", "프론트엔드"),
                        false,
                        342L,
                        55231L,
                        "출근하기"
                )
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/{targetId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("팔로잉 API 입니다.")
                        .summary("유저 팔로잉 API")
                        .pathParameters(
                            parameterWithName("targetId").description("대상 유저의 ID")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.nickname").description("닉네임"),
                                fieldWithPath("data.image").description("프로필 이미지 링크"),
                                fieldWithPath("data.title").description("대표 타이틀"),
                                fieldWithPath("data.introduction").description("자기소개"),
                                fieldWithPath("data.interests[]").description("관심사 리스트"),
                                fieldWithPath("data.follow").description("팔로잉 여부 (true: 팔로잉 상태, false: 언팔로잉 상태)"),
                                fieldWithPath("data.followerCount").description("팔로워 수"),
                                fieldWithPath("data.followingCount").description("팔로우 하는 수"),
                                fieldWithPath("data.challengeName").description("진행중인 챌린지 이름"))
                        .tag("MyPage")
                        .build()
        )));
    }

    @Test
    @DisplayName("최근 획득 타이틀 3개 조회")
    void my_page_titles() throws Exception {
        Long userId = 1L;
        Long total = 10L;
        LocalDateTime now = LocalDateTime.now();
        List<AchievedTitleResponse> achievedTitleResponses = List.of(
                AchievedTitleResponse.of(1000L, "시작이 반", "회원가입 시", now),
                AchievedTitleResponse.of(2000L, "위대한 첫 도약", "첫 인사이트 업로드", now.minusDays(1)),
                AchievedTitleResponse.of(2001L, "초보 기록가", "인사이트 5개", now.minusDays(3).minusMinutes(40))
        );
        MyPageTitleResponse response = MyPageTitleResponse.of(total, achievedTitleResponses);

        when(profileApiService.getMyPageTitles(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/achieved-title/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("최근 획득 타이틀 3개 조회 API 입니다.")
                        .summary("최근 획득 타이틀 3개 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.total").description("획득한 타이틀 총 개수"),
                                fieldWithPath("data.achievedTitles[].titleId").description("타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].name").description("타이틀의 이름"),
                                fieldWithPath("data.achievedTitles[].introduction").description("타이틀 소개"),
                                fieldWithPath("data.achievedTitles[].achievedDate").description("타이틀 획득 시각"))
                        .tag("MyPage")
                        .build()
        )));
    }

    @Test
    @DisplayName("획득 타이틀 전체 조회")
    void all_achieved_titles() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        List<AchievedTitleResponse> achievedTitleResponses = List.of(
                AchievedTitleResponse.of(1000L, "시작이 반", "회원가입 시", now),
                AchievedTitleResponse.of(2000L, "위대한 첫 도약", "첫 인사이트 업로드", now.minusDays(1)),
                AchievedTitleResponse.of(2001L, "초보 기록가", "인사이트 5개", now.minusDays(3).minusMinutes(40))
        );

        when(profileApiService.getAllAchievedTitles(anyLong()))
                .thenReturn(achievedTitleResponses);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/all-achieved-title/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("획득 타이틀 전체 조회 API 입니다.")
                        .summary("획득 타이틀 전체 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].titleId").description("타이틀의 ID"),
                                fieldWithPath("data[].name").description("타이틀의 이름"),
                                fieldWithPath("data[].introduction").description("타이틀 소개"),
                                fieldWithPath("data[].achievedDate").description("타이틀 획득 시각"))
                        .tag("MyPage")
                        .build()
        )));
    }
}
