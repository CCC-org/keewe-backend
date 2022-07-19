package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.common.LinkDto;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.service.user.ProfileService;
import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.common.enums.LinkType;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import ccc.keewedomain.domain.user.enums.UserStatus;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;

import static ccc.keewedomain.domain.common.enums.Activity.기타_음악;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileDocumentationTest extends ApiDocumentationTest {

    @InjectMocks
    ProfileController profileController;

    @Mock
    ProfileService profileService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(profileController, provider);
    }

    @Test
    @DisplayName("닉네임 생성 API")
    void create_nickname_test() throws Exception {
        // given
        String email = "test@keewe.com";
        String nickname = "\uD83C\uDDF0\uD83C\uDDF7\uD83D\uDE011한글B❣️✅#️⃣";
        Long profileId = 1L;

        User user = User.builder()
                .id(1L)
                .email(email)
                .status(UserStatus.ACTIVE)
                .deleted(false)
                .build();

        NicknameCreateRequest requestDto = new NicknameCreateRequest();
        requestDto.setNickname(nickname);
        requestDto.setProfileId(profileId);
        String token = "[유저의 JWT]";


        when(profileService.createNickname(any()))
                .thenReturn(NicknameCreateResponse.of(nickname, ProfileStatus.SOCIAL_LINK_NEEDED));


        mockMvc.perform(
                post("/api/v1/profiles/nickname")
                        .with(user(new UserPrincipal(user)))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile 온보딩 닉네임 생성 API 입니다.")
                                        .summary("닉네임 생성 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("nickname").description("생성할 닉네임"),
                                                fieldWithPath("profileId").description("대상 프로필의 id"))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.nickname").description("생성된 닉네임"),
                                                fieldWithPath("data.status")
                                                        .description("요청 완료 후 해당 프로필의 상태")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("링크 생성 api")
    void create_link_test() throws Exception {
        String link = "my._.link";
        Long profileId = 0L;

        User user = User.builder().build();

        LinkCreateRequest requestDto = new LinkCreateRequest(profileId, link);
        String token = "[유저의 JWT]";

        when(profileService.createLink(any()))
                .thenReturn(LinkCreateResponse.of(link, ProfileStatus.ACTIVITIES_NEEDED));

        mockMvc.perform(
                        post("/api/v1/profiles/link")
                                .with(user(new UserPrincipal(user)))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile 온보딩 링크 생성 API 입니다.")
                                        .summary("링크 생성 API입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("link").description("생성할 링크"),
                                                fieldWithPath("profileId").description("대상 프로필의 id"))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.link").description("생성된 링크"),
                                                fieldWithPath("data.status")
                                                        .description("요청 완료 후 해당 프로필의 상태")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("활동 분야 검색 api")
    void search_activities_test() throws Exception {
        String token = "[유저의 JWT]";

        when(profileService.searchActivities(any()))
                .thenReturn(new ActivitiesSearchResponse(List.of(기타_음악)));

        mockMvc.perform(
                        get("/api/v1/profiles/activities")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("keyword", "음악")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile 온보딩 활동 분야 검색 API 입니다.")
                                        .summary("활동 분야 검색 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestParameters(
                                                parameterWithName("keyword").description("검색어")
                                        )
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.activities")
                                                        .description("검색 결과")
                                                        .type("array")
                                                        .attributes(key("enumValues").value(List.of(Activity.values()))))
                                        .tag("Profile")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("소셜 링크 등록 API")
    void create_social_links_test() throws Exception {
        Long profileId = 1L;
        String token = "[유저의 JWT]";

        LinkDto linkDto1 = LinkDto.of("https://www.youtube.com/hello", "YOUTUBE");
        LinkDto linkDto2 = LinkDto.of("https://facebook.com/world", "FACEBOOK");

        List<LinkDto> linkDtos = List.of(linkDto1, linkDto2);


        SocialLinkCreateRequest requestDto = new SocialLinkCreateRequest();
        requestDto.setProfileId(profileId);
        requestDto.setLinks(linkDtos);

        mockMvc.perform(
                        post("/api/v1/profiles/social-links")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile 온보딩 소셜 링크 생성 API 입니다.")
                                        .summary("소셜 링크 생성 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("profileId").description("대상 프로필의 id"),
                                                fieldWithPath("links[].url").description("등록할 주소"),
                                                fieldWithPath("links[].type")
                                                        .description("등록할 주소의 타입")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(LinkType.values()))))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data").description("비어 있음"))
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("온보딩 미수행 프로필 조회 api")
    void incomplete_profile_select() throws Exception {
        when(profileService.getIncompleteProfile()).thenReturn(IncompleteProfileResponse.getExistResult(Profile.builder()
                        .id(1L)
                        .profileStatus(ProfileStatus.NICKNAME_NEEDED)
                        .build()));

        mockMvc.perform(get("/api/v1/profiles/incomplete")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "[유저의 JWT]")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile 온보딩 미수행 프로필 검색 API 입니다.")
                                        .summary("온보딩 미수행 프로필 검색 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.exist").description("미완성 프로필 존재 여부"),
                                                fieldWithPath("data.profileId").description("미완성 프로필 ID"),
                                                fieldWithPath("data.status").description("온보딩 수행해야 할 단계")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));

    }
}
