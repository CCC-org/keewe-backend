package ccc.keeweapi.api.user;

import ccc.keeweapi.document.utils.RestDocsTestSupport;
import ccc.keeweapi.dto.user.UserSignUpDto;
import ccc.keeweapi.service.user.ProfileService;
import ccc.keeweapi.service.user.UserApiService;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.SocialLinkDomainService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

public class UserSignupControllerTest extends RestDocsTestSupport {
    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserApiService userApiService;

    @MockBean
    private ProfileDomainService profileDomainService;

    @MockBean
    private SocialLinkDomainService socialLinkDomainService;

    @Test
    @DisplayName("카카오 회원가입/로그인")
    void kakao_signup() throws Exception {
        // given
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setUserId(1L);
        userSignUpDto.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXZlYWx3YXlzYmVlbkBrYWthby5jb20iLCJyb2xlcyI6W10sImlhdCI6MTY1NzQzNjU2MywiZXhwIjoxNjU3Nzk2NTYzfQ.AJX7rGRXjmi4TopUBsX6zWVgMYgjN_uRYtF_Yb_80KE");

        when(userApiService.signUpWithKakao(anyString())).thenReturn(userSignUpDto);

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
                                        fieldWithPath("data.accessToken").description("요청 완료 후 해당 프로필의 상태"))
                                .tag("SignUp")
                                .build()
                )));
    }
}
