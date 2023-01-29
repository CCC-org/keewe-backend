package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.domain.common.Interest;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
                        .description("마이페이지 프로필 조회 API 입니다.")
                        .summary("마이페이지 프로필 조회 API")
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

        AllAchievedTitleResponse response = AllAchievedTitleResponse.of(1000L, achievedTitleResponses);

        when(profileApiService.getAllAchievedTitles(anyLong()))
                .thenReturn(response);

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
                                fieldWithPath("data.repTitleId").description("대표 타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].titleId").description("타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].name").description("타이틀의 이름"),
                                fieldWithPath("data.achievedTitles[].introduction").description("타이틀 소개"),
                                fieldWithPath("data.achievedTitles[].achievedDate").description("타이틀 획득 시각"))
                        .tag("MyPage")
                        .build()
        )));
    }

    @Test
    @DisplayName("팔로워 목록 조회")
    void get_followers() throws Exception {
        Long userId = 1L;
        long limit = 10L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cursor = now.minusDays(1);
        List<FollowUserResponse> followUserResponseList = List.of(
                FollowUserResponse.of(1L, "hello", "www.api-keewe.com/images/128398681", "시작이 반", true),
                FollowUserResponse.of(2L, "world", "www.api-keewe.com/images/128398681", "위대한 첫 도약", false)
        );

        FollowUserListResponse response = FollowUserListResponse.of(Optional.ofNullable(cursor), followUserResponseList);

        when(profileApiService.getFollowers(anyLong(), any()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/follower/{userId}", userId)
                        .param("cursor", cursor.toString())
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("팔로워 목록 조회 API 입니다")
                        .summary("팔로워 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("페이징을 위한 커서 yyyy-MM-dd'T'hh:mm:ss.SSS"),
                                parameterWithName("limit").description("한번에 조회할 개수"))
                        .responseFields(followUserListResponseFields())
                        .tag("MyPage")
                        .build()
        )));
    }

    @Test
    @DisplayName("팔로이 목록 조회")
    void get_followees() throws Exception {
        Long userId = 1L;
        long limit = 10L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cursor = now.minusDays(1);
        List<FollowUserResponse> followUserResponseList = List.of(
                FollowUserResponse.of(1L, "hello", "www.api-keewe.com/images/128398681", "시작이 반", true),
                FollowUserResponse.of(2L, "world", "www.api-keewe.com/images/128398681", "위대한 첫 도약", false)
        );

        FollowUserListResponse response = FollowUserListResponse.of(Optional.ofNullable(cursor), followUserResponseList);

        when(profileApiService.getFollowees(anyLong(), any()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/followee/{userId}", userId)
                        .param("cursor", cursor.toString())
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("팔로이 목록 조회 API 입니다")
                        .summary("팔로이 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("페이징을 위한 커서 yyyy-MM-dd'T'hh:mm:ss.SSS"),
                                parameterWithName("limit").description("한번에 조회할 개수"))
                        .responseFields(followUserListResponseFields())
                        .tag("MyPage")
                        .build()
        )));
    }

    public List<FieldDescriptor> followUserListResponseFields() {
        return List.of(
                fieldWithPath("message").description("요청 결과 메세지"),
                fieldWithPath("code").description("결과 코드"),
                fieldWithPath("data.minCreatedAt").description("페이징 커서 yyyy-MM-dd'T'hh:mm:ss.SSS 포맷"),
                fieldWithPath("data.users[].id").description("유저의 ID"),
                fieldWithPath("data.users[].nickname").description("유저 닉네임"),
                fieldWithPath("data.users[].imageURL").description("프로필 이미지 URL"),
                fieldWithPath("data.users[].title").description("대표 타이틀 제목"),
                fieldWithPath("data.users[].follow").description("팔로우 여부")
        );
    }

    @Test
    @DisplayName("사용자 차단")
    void block() throws Exception {
        long targetId = 2L;
        BlockUserResponse response = BlockUserResponse.of(targetId);

        when(profileApiService.blockUser(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/profile/block/{targetId}", targetId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("사용자 차단 API 입니다")
                        .summary("사용자 차단 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.blockedUserId").description("차단된 유저의 ID"))
                        .tag("Profile")
                        .build()
        )));
    }

    @Test
    @DisplayName("사용자 차단 해제")
    void unblock() throws Exception {
        long targetId = 2L;
        UnblockUserResponse response = UnblockUserResponse.of(targetId);

        when(profileApiService.unblockUser(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/profile/block/{targetId}", targetId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("사용자 차단 해제 API 입니다")
                        .summary("사용자 차단 해제 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.unblockedUserId").description("차단 해제된 유저의 ID"))
                        .tag("Profile")
                        .build()
        )));
    }

    @Test
    @DisplayName("나의 차단 리스트 조회")
    void get_my_block_list() throws Exception {
        MyBlockUserListResponse response = MyBlockUserListResponse.of(List.of(
                MyBlockUserResponse.of(1L, "hello","시작이 반", "www.api-keewe.com/images/128398681"),
                MyBlockUserResponse.of(2L, "world","위대한 첫 도약", "www.api-keewe.com/images/128398681")
        ));

        when(profileApiService.getMyBlockList())
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/my-block-list")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("나의 차단 목록 조회 API 입니다")
                        .summary("나의 차단 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.blockedUsers[]").description("차단한 유저 목록"),
                                fieldWithPath("data.blockedUsers[].id").description("유저의 ID"),
                                fieldWithPath("data.blockedUsers[].nickname").description("유저의 닉네임"),
                                fieldWithPath("data.blockedUsers[].title").description("대표 타이틀"),
                                fieldWithPath("data.blockedUsers[].imageURL").description("프로필 이미지 URL")
                        )
                        .tag("Profile")
                        .build()
        )));
    }

    @Test
    @DisplayName("프로필 변경 API")
    void update_profile() throws Exception {
        ProfileUpdateResponse response = ProfileUpdateResponse.of("https://keewe-image-bucket-for-local.s3.ap-northeast-2.amazonaws.com/4196ce1a-7cef-4426-9c43-b0d5c2f9a6dd-img.jpeg");

        when(profileApiService.updateProfile(any()))
                .thenReturn(response);

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/v1/user/profile");
        builder.with(request -> {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        });

        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "image.jpg".getBytes());

        ResultActions resultActions = mockMvc.perform(builder

                        .file(profileImage)
                        .param("nickname", "새 닉네임")
                        .param("interests", "백엔드")
                        .param("interests", "프론트엔드")
                        .param("repTitleId", "1000")
                        .param("introduction", "자기소개")
                        .param("deletePhoto", "false")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("프로필 변경 API 입니다")
                        .summary("프로필 변경 API")
                        .requestParameters(
                                parameterWithName("nickname").description("변경할 닉네임"),
                                parameterWithName("interests").description("변경할 관심사 리스트"),
                                parameterWithName("repTitleId").type(SimpleType.NUMBER).description("변경할 대표 타이틀 ID"),
                                parameterWithName("introduction").description("변경할 자기 소개"),
                                parameterWithName("deletePhoto").type(SimpleType.BOOLEAN).description("프로필 사진 삭제 여부(true/false)")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.imageURL").description("변경된 프로필 이미지 URL"))
                        .tag("Profile")
                        .build()
        )));
    }
}
