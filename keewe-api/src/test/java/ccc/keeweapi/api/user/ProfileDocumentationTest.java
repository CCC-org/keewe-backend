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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ccc.keewedomain.domain.common.enums.Activity.*;
import static ccc.keewedomain.domain.user.enums.ProfileStatus.*;
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
    @DisplayName("????????? ?????? API")
    void create_nickname_test() throws Exception {
        // given
        String email = "test@keewe.com";
        String nickname = "\uD83C\uDDF0\uD83C\uDDF7\uD83D\uDE011??????B?????????#??????";
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


        when(profileService.createNickname(any()))
                .thenReturn(NicknameCreateResponse.of(nickname, ACTIVITIES_NEEDED));


        mockMvc.perform(
                        post("/api/v1/profiles/nickname")
                                .with(user(new UserPrincipal(user)))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ????????? ?????? API ?????????.")
                                        .summary("????????? ?????? API ?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .requestFields(
                                                fieldWithPath("nickname").description("????????? ?????????"),
                                                fieldWithPath("profileId").description("?????? ???????????? id"))
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data.nickname").description("????????? ?????????"),
                                                fieldWithPath("data.status")
                                                        .description("?????? ?????? ??? ?????? ???????????? ??????")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("?????? ?????? api")
    void create_link_test() throws Exception {
        String link = "my._.link";
        Long profileId = 0L;

        User user = User.builder().build();

        LinkCreateRequest requestDto = new LinkCreateRequest(profileId, link);

        when(profileService.createLink(any()))
                .thenReturn(LinkCreateResponse.of(link, ProfileStatus.ACTIVITIES_NEEDED));

        mockMvc.perform(
                        post("/api/v1/profiles/link")
                                .with(user(new UserPrincipal(user)))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ?????? ?????? API ?????????.")
                                        .summary("?????? ?????? API?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .requestFields(
                                                fieldWithPath("link").description("????????? ??????"),
                                                fieldWithPath("profileId").description("?????? ???????????? id"))
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data.link").description("????????? ??????"),
                                                fieldWithPath("data.status")
                                                        .description("?????? ?????? ??? ?????? ???????????? ??????")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? api")
    void create_activities_test() throws Exception {
        List<String> activities = List.of("INDIE", "POP");

        User user = User.builder().build();
        ActivitiesCreateRequest requestDto = new ActivitiesCreateRequest(user.getId(), activities);

        when(profileService.createActivities(any()))
                .thenReturn(ActivitiesCreateResponse.of(activities.stream().map((a) -> Activity.valueOf(a).getValue()).collect(Collectors.toList()), LINK_NEEDED));

        mockMvc.perform(
                        post("/api/v1/profiles/activities")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ?????? ?????? ?????? API ?????????.")
                                        .summary("?????? ?????? ?????? API?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .requestFields(
                                                fieldWithPath("activities")
                                                        .description("????????? ?????? ????????? ?????????")
                                                        .type("array")
                                                        .attributes(key("enumValues").value(List.of(Activity.values()))),
                                                fieldWithPath("profileId").description("?????? ???????????? id"))
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data.activities")
                                                        .description("?????? ?????? ??????")
                                                        .type("array")
                                                        .attributes(key("enumValues").value(List.of(Arrays.stream(Activity.values()).map(Activity::getValue).collect(Collectors.toList())))),
                                                fieldWithPath("data.status")
                                                        .description("?????? ?????? ??? ?????? ???????????? ??????")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values())))
                                        )
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ?????? api")
    void search_activities_test() throws Exception {

        when(profileService.searchActivities(any()))
                .thenReturn(new ActivitiesSearchResponse(List.of(OTHER_MUSIC)));

        mockMvc.perform(
                        get("/api/v1/profiles/activities")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .param("keyword", "??????")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ?????? ?????? ?????? API ?????????.")
                                        .summary("?????? ?????? ?????? API ?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .requestParameters(
                                                parameterWithName("keyword").description("?????????")
                                        )
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data.activities")
                                                        .description("?????? ??????")
                                                        .type("array")
                                                        .attributes(key("enumValues").value(List.of(Activity.values()))))
                                        .tag("Profile")
                                        .build()
                        )));

    }

    @Test
    @DisplayName("?????? ?????? ?????? API")
    void create_social_links_test() throws Exception {
        Long profileId = 1L;

        LinkDto linkDto1 = LinkDto.of("https://www.youtube.com/hello", "YOUTUBE");
        LinkDto linkDto2 = LinkDto.of("https://facebook.com/world", "FACEBOOK");

        List<LinkDto> linkDtos = List.of(linkDto1, linkDto2);


        SocialLinkCreateRequest requestDto = new SocialLinkCreateRequest();
        requestDto.setProfileId(profileId);
        requestDto.setLinks(linkDtos);

        mockMvc.perform(
                        post("/api/v1/profiles/social-links")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ?????? ?????? ?????? API ?????????.")
                                        .summary("?????? ?????? ?????? API ?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .requestFields(
                                                fieldWithPath("profileId").description("?????? ???????????? id"),
                                                fieldWithPath("links[].url").description("????????? ??????"),
                                                fieldWithPath("links[].type")
                                                        .description("????????? ????????? ??????")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(LinkType.values()))))
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data").description("?????? ??????"))
                                        .tag("Profile")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? api")
    void incomplete_profile_select() throws Exception {
        when(profileService.getIncompleteProfile()).thenReturn(IncompleteProfileResponse.getExistResult(Profile.builder()
                        .id(1L)
                        .profileStatus(ProfileStatus.NICKNAME_NEEDED)
                        .build()));

        mockMvc.perform(get("/api/v1/profiles/incomplete")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Profile ????????? ????????? ????????? ?????? API ?????????.")
                                        .summary("????????? ????????? ????????? ?????? API ?????????.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("????????? JWT"))
                                        .responseFields(
                                                fieldWithPath("message").description("?????? ?????? ?????????"),
                                                fieldWithPath("code").description("?????? ??????"),
                                                fieldWithPath("data.exist").description("????????? ????????? ?????? ??????"),
                                                fieldWithPath("data.profileId").description("????????? ????????? ID"),
                                                fieldWithPath("data.status").description("????????? ???????????? ??? ??????")
                                                        .type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(ProfileStatus.values()))))
                                        .tag("Profile")
                                        .build()
                        )));

    }
}
