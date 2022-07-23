package ccc.keeweapi.document.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled
@Import(RestDocsConfig.class)
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class ApiDocumentationTest {

    protected MockMvc mockMvc;

    @Spy
    protected ObjectMapper objectMapper;

    protected static RestDocumentationResultHandler restDocs;

    @BeforeAll
    static void restDocSetup() {
        restDocs = MockMvcRestDocumentation.document("{class-name}/{method-name}");
    }

    public void setup(Object controller, RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
