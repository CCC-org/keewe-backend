package ccc.keeweapi.controller.api.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.AccountResponse;
import ccc.keeweapi.dto.user.InterestsResponse;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.dto.user.ProfileMyPageResponse;
import ccc.keeweapi.dto.user.ProfileUpdateResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

public class UserProfileControllerTest extends ApiDocumentationTest {

    @InjectMocks
    UserProfileController userProfileController;

    @Mock
    ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userProfileController, provider);
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
                        .tag("UserProfile")
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
                        .tag("UserProfile")
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
                        .param("updatePhoto", "false")
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
                                parameterWithName("updatePhoto").type(SimpleType.BOOLEAN).description("프로필 사진 업데이트 여부(true/false)")
                        )
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.imageURL").description("변경된 프로필 이미지 URL"))
                        .tag("UserProfile")
                        .build()
        )));
    }

    @Test
    @DisplayName("관심사 조회 테스트")
    void get_interests() throws Exception {
        when(profileApiService.getInterests()).thenReturn(
                InterestsResponse.of(List.of(
                        "주식",
                        "운동"
                ))
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/interests")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("관심사 조회 API 입니다.")
                        .summary("관심사 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.interests").description("관심사 리스트"))
                        .tag("UserProfile")
                        .build()
        )));
    }

    @Test
    @DisplayName("연결된 계정 조회 API")
    void get_related_account() throws Exception {
        when(profileApiService.getAccount()).thenReturn(
                AccountResponse.of(VendorType.NAVER, "helloworld@naver.com")
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/account")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("연결된 계정 조회 API 입니다.")
                        .summary("연결된 계정 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.vendorType").description("OAuth vendor의 타입").type("ENUM").attributes(key("enumValues").value(VendorType.values())),
                                fieldWithPath("data.identifier").description("유저의 식별자. 1순위 email, 2순위 vendorId"))
                        .tag("UserProfile")
                        .build()
        )));
    }
}
