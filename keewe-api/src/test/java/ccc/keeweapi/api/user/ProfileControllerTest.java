package ccc.keeweapi.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.OnboardResponse;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
}
