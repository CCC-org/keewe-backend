package ccc.keeweapi.controller.api.user;

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
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.FollowUserListResponse;
import ccc.keeweapi.dto.user.FollowUserResponse;
import ccc.keeweapi.dto.user.InviteeListResponse;
import ccc.keeweapi.dto.user.InviteeResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

public class UserRelationControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private UserRelationController userRelationController;

    @Mock
    private ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userRelationController, provider);
    }

    @Test
    @DisplayName("팔로잉")
    void following_test() throws Exception {
        when(profileApiService.toggleFollowership(anyLong(), anyLong())).thenReturn(
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
                        .tag("UserRelation")
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
                        .tag("UserRelation")
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
                        .tag("UserRelation")
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
}
