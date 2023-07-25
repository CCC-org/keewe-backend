package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.service.user.command.UserCommandApiService;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserTokenControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private UserTokenController userTokenController;

    @Mock
    private UserCommandApiService userCommandApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userTokenController, provider);
    }

    @Test
    @DisplayName("Push 토큰 등록")
    void register_push_token() throws Exception {
        JSONObject registerPushTokenRequest = new JSONObject();
        registerPushTokenRequest.put("pushToken", "THIS_IS_INVALID_TOKEN");

        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/token/push")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(registerPushTokenRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("Push 토큰 등록 API 입니다.")
                        .summary("Push 토큰 등록 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("pushToken").description("등록할 push token")
                        )
                        .tag("UserTitle")
                        .build()
        )));
    }
}
