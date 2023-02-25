package ccc.keeweapi.controller.api.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.BlockUserResponse;
import ccc.keeweapi.dto.user.MyBlockUserListResponse;
import ccc.keeweapi.dto.user.MyBlockUserResponse;
import ccc.keeweapi.dto.user.UnblockUserResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

public class UserBlockControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private UserBlockController userBlockController;

    @Mock
    private ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userBlockController, provider);
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
}

