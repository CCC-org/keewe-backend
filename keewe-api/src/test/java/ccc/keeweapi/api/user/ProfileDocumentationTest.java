package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.UserDetailService;
import ccc.keeweapi.config.security.UserPrincipal;
import ccc.keeweapi.document.utils.RestDocsTestSupport;
import ccc.keeweapi.dto.user.LinkCreateRequestDto;
import ccc.keeweapi.dto.user.LinkCreateResponseDto;
import ccc.keeweapi.dto.user.NicknameCreateRequestDto;
import ccc.keeweapi.dto.user.NicknameCreateResponseDto;
import ccc.keeweapi.service.user.ProfileService;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import ccc.keewedomain.domain.user.enums.UserStatus;
import ccc.keewedomain.service.ProfileDomainService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileDocumentationTest extends RestDocsTestSupport {

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileDomainService profileDomainService;

    @MockBean
    private UserDetailService userDetailsService;

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

        NicknameCreateRequestDto requestDto = new NicknameCreateRequestDto();
        requestDto.setNickname(nickname);
        requestDto.setProfileId(profileId);
        String token = jwtUtils.createToken(email, new ArrayList<>());


        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(new UserPrincipal(user));

        when(profileService.createNickname(any(), any()))
                .thenReturn(new NicknameCreateResponseDto(nickname, ProfileStatus.SOCIAL_LINK_NEEDED));


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
        String email = "test@keewe.com";
        String link = "my._.link";
        Long profileId = 0L;

        User user = User.builder().build();

        LinkCreateRequestDto requestDto = new LinkCreateRequestDto(profileId, link);
        String token = jwtUtils.createToken(email, new ArrayList<>());


        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(new UserPrincipal(user));

        when(profileService.createLink(any(), any()))
                .thenReturn(new LinkCreateResponseDto(link, ProfileStatus.ACTIVITIES_NEEDED));


        mockMvc.perform(
                        post("/api/v1/profiles/link")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("profile 온보딩 링크 생성 API")
                                        .summary("링크 생성 api")
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


}
