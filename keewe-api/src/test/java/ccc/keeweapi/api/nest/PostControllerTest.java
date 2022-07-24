package ccc.keeweapi.api.nest;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.nest.AnnouncementCreateRequest;
import ccc.keeweapi.dto.nest.AnnouncementCreateResponse;
import ccc.keeweapi.service.post.PostService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends ApiDocumentationTest {

    @InjectMocks
    PostController postController;

    @Mock
    PostService postService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(postController, provider);
    }

    @Test
    @DisplayName("공지 생성 API")
    void announcement_create_test() throws Exception {

        String token = "[유저의 JWT]";
        AnnouncementCreateRequest request = new AnnouncementCreateRequest();
        request.setProfileId(1L);
        request.setContent("안녕하세요 내용이에요.");

        when(postService.createAnnouncementPost(any()))
                .thenReturn(AnnouncementCreateResponse.of(1L));

        mockMvc.perform(
                        post("/api/v1/nest/announcement")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("공지 생성 API 입니다.")
                                        .summary("공지 생성 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("profileId").description("대상 프로필의 id"),
                                                fieldWithPath("content").description("글의 내용"))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.postId").description("작성된 공지의 ID")
                                        )
                                        .tag("Post")
                                        .build()
                        )));
    }
}
