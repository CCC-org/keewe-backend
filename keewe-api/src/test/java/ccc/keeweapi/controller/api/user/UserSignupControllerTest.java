package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keeweapi.service.user.UserApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSignupControllerTest extends ApiDocumentationTest {

    @InjectMocks
    private UserSignUpController userSignUpController;

    @Mock
    private UserApiService userApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userSignUpController, provider);
    }

    @Test
    @DisplayName("카카오 회원가입/로그인")
    void kakao_signup() throws Exception {
        // given
        UserSignUpResponse userSignUpDto = UserSignUpResponse.of(
                1L,
                true,
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXZlYWx3YXlzYmVlbkBrYWthby5jb20iLCJyb2xlcyI6W10sImlhdCI6MTY1NzQzNjU2MywiZXhwIjoxNjU3Nzk2NTYzfQ.AJX7rGRXjmi4TopUBsX6zWVgMYgjN_uRYtF_Yb_80KE"
        );


        when(userApiService.signupWithOauth(anyString(), any())).thenReturn(userSignUpDto);

        mockMvc.perform(
                        get("/api/v1/user/kakao")
                                .param("code", "OcIq1_RZnzHNZ54ylZcBunZGiqcGf7g4IiMVi8WVJgxZkTcfg0dOJK4pgysxSwL_sO_2Lgo9dNkAAAGB5urHyg")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("카카오 회원가입/로그인 API 입니다.")
                                .summary("카카오 회원가입/로그인 API 입니다.")
                                .requestParameters(parameterWithName("code").description("인가 코드 (Authorization Code)"))
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.userId").description("생성된 유저 ID"),
                                        fieldWithPath("data.alreadySignedUp").description("기존 회원가입 여부"),
                                        fieldWithPath("data.accessToken").description("발급된 유저의 JWT"))
                                .tag("UserSignUp")
                                .build()
                )));
    }

    @Test
    @DisplayName("네이버 회원가입/로그인")
    void naver_signup() throws Exception {
        final String naverState = "oauth_state";
        String state = "123456789";
        UserSignUpResponse userSignUpDto = UserSignUpResponse.of(
                1L,
                true,
                "[발급된 JWT]"
        );

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(naverState, state);

        when(userApiService.signupWithOauth(anyString(), any())).thenReturn(userSignUpDto);

        mockMvc.perform(
                        get("/api/v1/user/naver")
                                .session(session)
                                .param("code", "[네이버에서 받은 code]")
                                .param("state", state)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("네이버 회원가입/로그인 API 입니다.")
                                .summary("네이버 회원가입/로그인 API 입니다.")
                                .requestParameters(
                                        parameterWithName("code").description("인가 코드 (Authorization Code)"),
                                        parameterWithName("state").description("CSRF 공격을 방지하기 위한 state")
                                )
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.userId").description("생성된 유저 ID"),
                                        fieldWithPath("data.alreadySignedUp").description("기존 회원가입 여부"),
                                        fieldWithPath("data.accessToken").description("발급된 유저의 JWT"))
                                .tag("UserSignUp")
                                .build()
                )));
    }

    @Test
    @DisplayName("구글 회원가입/로그인")
    void google_signup() throws Exception {
        UserSignUpResponse userSignUpDto = UserSignUpResponse.of(
                1L,
                true,
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXZlYWx3YXlzYmVlbkBrYWthby5jb20iLCJyb2xlcyI6W10sImlhdCI6MTY1NzQzNjU2MywiZXhwIjoxNjU3Nzk2NTYzfQ.AJX7rGRXjmi4TopUBsX6zWVgMYgjN_uRYtF_Yb_80KE"
        );


        when(userApiService.signupWithOauth(anyString(), any())).thenReturn(userSignUpDto);

        mockMvc.perform(
                        get("/api/v1/user/google")
                                .param("code", "OcIq1_RZnzHNZ54ylZcBunZGiqcGf7g4IiMVi8WVJgxZkTcfg0dOJK4pgysxSwL_sO_2Lgo9dNkAAAGB5urHyg")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("구글 회원가입/로그인 API 입니다.")
                                .summary("구글 회원가입/로그인 API 입니다.")
                                .requestParameters(parameterWithName("code").description("인가 코드 (Authorization Code)"))
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.userId").description("생성된 유저 ID"),
                                        fieldWithPath("data.alreadySignedUp").description("기존 회원가입 여부"),
                                        fieldWithPath("data.accessToken").description("발급된 유저의 JWT"))
                                .tag("UserSignUp")
                                .build()
                )));
    }

    @Test
    @DisplayName("애플 회원가입/로그인")
    void apple_signup() throws Exception {
        UserSignUpResponse userSignUpDto = UserSignUpResponse.of(
                1L,
                true,
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXZlYWx3YXlzYmVlbkBrYWthby5jb20iLCJyb2xlcyI6W10sImlhdCI6MTY1NzQzNjU2MywiZXhwIjoxNjU3Nzk2NTYzfQ.AJX7rGRXjmi4TopUBsX6zWVgMYgjN_uRYtF_Yb_80KE"
        );


        when(userApiService.signupWithOauth(anyString(), any())).thenReturn(userSignUpDto);

        mockMvc.perform(
                        get("/api/v1/user/apple")
                                .param("code", "OcIq1_RZnzHNZ54ylZcBunZGiqcGf7g4IiMVi8WVJgxZkTcfg0dOJK4pgysxSwL_sO_2Lgo9dNkAAAGB5urHyg")
                ).andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("애플 회원가입/로그인 API 입니다.")
                                .summary("애플 회원가입/로그인 API 입니다.")
                                .requestParameters(parameterWithName("code").description("인가 코드 (Authorization Code)"))
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.userId").description("생성된 유저 ID"),
                                        fieldWithPath("data.alreadySignedUp").description("기존 회원가입 여부"),
                                        fieldWithPath("data.accessToken").description("발급된 유저의 JWT"))
                                .tag("UserSignUp")
                                .build()
                )));
    }
}
