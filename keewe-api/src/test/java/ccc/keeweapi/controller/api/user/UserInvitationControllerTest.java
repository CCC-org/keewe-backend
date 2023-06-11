package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.InviteeListResponse;
import ccc.keeweapi.dto.user.InviteeResponse;
import ccc.keeweapi.dto.user.InviteeSearchResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserInvitationControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private UserInvitationController userInvitationController;

    @Mock
    private ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userInvitationController, provider);
    }

    @Test
    @DisplayName("초대 유저 목록 조회 API")
    void get_related_users() throws Exception {
        long limit = 10L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cursor = now.minusDays(1);
        List<InviteeResponse> inviteeResponse = List.of(
                InviteeResponse.of(1L, "hello", "www.api-keewe.com/images/128398681"),
                InviteeResponse.of(2L, "world", "www.api-keewe.com/images/128398681")
        );

        InviteeListResponse response = InviteeListResponse.of(cursor.toString(), inviteeResponse);

        when(profileApiService.paginateInvitees(any()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/invitee")
                        .param("cursor", cursor.toString())
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("초대 유저 목록 조회 API 입니다")
                        .summary("초대 유저 목록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("페이징을 위한 커서 yyyy-MM-dd'T'hh:mm:ss.SSS"),
                                parameterWithName("limit").description("한번에 조회할 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.nextCursor").description("다음 조회를 위한 커서. 없을 경우 null"),
                                fieldWithPath("data.invitees[].userId").description("유저의 ID"),
                                fieldWithPath("data.invitees[].nickname").description("유저의 닉네임"),
                                fieldWithPath("data.invitees[].imageURL").description("유저의 프로필 이미지 URL")
                        )
                        .tag("UserInvitation")
                        .build()
        )));
    }

    @Test
    @DisplayName("초대 유저 닉네임 검색 API")
    void search_related_users() throws Exception {
        long limit = 10L;
        String cursor = "닉네임:1";
        String searchWord = "닉네";
        List<InviteeResponse> inviteeResponse = List.of(
                InviteeResponse.of(1L, "hello", "www.api-keewe.com/images/128398681"),
                InviteeResponse.of(2L, "world", "www.api-keewe.com/images/128398681")
        );

        InviteeSearchResponse response = InviteeSearchResponse.of(cursor, inviteeResponse);

        when(profileApiService.searchInvitees(any(), any()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/invitee/search")
                        .param("searchWord", searchWord)
                        .param("cursor", cursor)
                        .param("limit", Long.toString(limit))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("초대 유저 닉네임 검색 API 입니다")
                        .summary("초대 유저 닉네임 검색 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("searchWord").description("검색할 닉네임 prefix"),
                                parameterWithName("cursor").description("페이징을 위한 커서. 응답의 nextCursor. 첫 요청엔 미포함").optional(),
                                parameterWithName("limit").description("한번에 조회할 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.nextCursor").description("다음 조회를 위한 커서. 없을 경우 null"),
                                fieldWithPath("data.invitees[].userId").description("유저의 ID"),
                                fieldWithPath("data.invitees[].nickname").description("유저의 닉네임"),
                                fieldWithPath("data.invitees[].imageURL").description("유저의 프로필 이미지 URL")
                        )
                        .tag("UserInvitation")
                        .build()
        )));
    }
}
