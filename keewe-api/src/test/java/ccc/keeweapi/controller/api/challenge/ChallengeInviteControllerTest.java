package ccc.keeweapi.controller.api.challenge;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.service.challenge.command.ChallengeInviteApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeInviteControllerTest extends ApiDocumentationTest {

    @InjectMocks
    ChallengeInviteController challengeInviteController;

    @Mock
    ChallengeInviteApiService challengeInviteApiService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(challengeInviteController, provider);
    }

    @Test
    @DisplayName("챌린지 초대")
    void invite_challenge() throws Exception {
        long challengeId = 0L;
        JSONObject request = new JSONObject();
        request.put("challengeId", challengeId)
                .put("targetUserId", 1L);

        doNothing().when(challengeInviteApiService).sendInvite(anyLong(), anyLong());

        ResultActions resultActions = mockMvc.perform(post("/api/v1/challenge/invite")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(request.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("챌린지 초대 API입니다.")
                        .summary("챌린지 초대 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("challengeId").description("챌린지 ID"),
                                fieldWithPath("targetUserId").description("초대 보낼 유저 ID"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("데이터"))
                        .tag("ChallengeInvite")
                        .build()
        )));
    }
}
