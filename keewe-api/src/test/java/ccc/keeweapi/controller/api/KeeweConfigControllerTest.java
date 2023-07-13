package ccc.keeweapi.controller.api;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keewedomain.persistence.domain.misc.KeeweConfig;
import ccc.keewedomain.persistence.repository.misc.KeeweConfigRepository;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KeeweConfigControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private KeeweConfigController keeweConfigController;

    @Mock
    private KeeweConfigRepository keeweConfigRepository;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(keeweConfigController, provider);
    }

    @Test
    @DisplayName("로그인 설정 정보 조회")
    void get_my_block_list() throws Exception {
        when(keeweConfigRepository.findById(anyString()))
                .thenReturn(Optional.of(new KeeweConfig("ALLOWED_OAUTH_VENDORS", "KAKAO,NAVER,GOOGLE,APPLE")));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/config/allowed-oauth-vendor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("로그인 노출 설정 정보")
                        .summary("로그인 노출 설정 정보 API")
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.vendors[]").description("허용 벤더 목록")
                        )
                        .tag("KeeweConfig")
                        .build()
        )));
    }
}

