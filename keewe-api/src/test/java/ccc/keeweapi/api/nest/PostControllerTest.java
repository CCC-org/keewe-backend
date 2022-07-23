package ccc.keeweapi.api.nest;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.nest.PostResponse;
import ccc.keeweapi.dto.nest.VotePostCreateRequest;
import ccc.keeweapi.service.nest.PostApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import java.util.ArrayList;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends ApiDocumentationTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostApiService postApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(postController, provider);
    }

    @Test
    @DisplayName("둥지 - 투표글 생성 API")
    void create_vote_post() throws Exception {
        VotePostCreateRequest request = new VotePostCreateRequest();
        request.setProfileId(1L);
        request.setContents("똥 vs 카레");
        request.setCandidates(new ArrayList<>());
        request.getCandidates().add("똥");
        request.getCandidates().add("카레");

        when(postApiService.createPost(any())).thenReturn(new PostResponse(1L));

        mockMvc.perform(post("/api/v1/nest/vote")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "[유저의 JWT]")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("둥지 - 투표글 생성 API 입니다.")
                                .summary("투표글 생성 API 입니다.")
                                .requestHeaders(
                                        headerWithName("Authorization").description("유저의 JWT"))
                                .requestFields(
                                        fieldWithPath("profileId").description("대상 프로필의 id"),
                                        fieldWithPath("candidates[]").description("생성할 투표 선택지"),
                                        fieldWithPath("contents").description("게시글 내용"))
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.postId").description("생성된 게시글 ID"))
                                .tag("Nest")
                                .build()
                )));
    }
}
