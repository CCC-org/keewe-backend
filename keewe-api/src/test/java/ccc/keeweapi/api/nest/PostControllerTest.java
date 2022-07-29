package ccc.keeweapi.api.nest;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.nest.PostCreateRequest;
import ccc.keeweapi.dto.nest.PostResponse;
import ccc.keeweapi.dto.nest.VotePostCreateRequest;
import ccc.keeweapi.service.nest.PostApiService;
import ccc.keewedomain.domain.nest.enums.PostType;
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
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends ApiDocumentationTest {

    @InjectMocks
    PostController postController;

    @Mock
    PostApiService postApiService;

    @BeforeEach
    void setup(final RestDocumentationContextProvider provider) {
        super.setup(postController, provider);
    }

    @Test
    @DisplayName("둥지 - 투표글 생성 API")
    void create_vote_post() throws Exception {
        VotePostCreateRequest request = new VotePostCreateRequest();
        request.setProfileId(1L);
        request.setContent("똥 vs 카레");
        request.setPostType(PostType.VOTE.name());
        request.setCandidates(new ArrayList<>());
        request.getCandidates().add("똥");
        request.getCandidates().add("카레");

        when(postApiService.createPost(any())).thenReturn(PostResponse.of(1L));

        mockMvc.perform(post("/api/v1/nest/post")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .description("둥지 글 생성 API 입니다.")
                                .summary("둥지 글 생성 API 입니다.")
                                .requestHeaders(
                                        headerWithName("Authorization").description("유저의 JWT"))
                                .requestFields(
                                        fieldWithPath("profileId").description("대상 프로필의 id"),
                                        fieldWithPath("candidates[]").description("생성할 투표 선택지"),
                                        fieldWithPath("content").description("게시글 내용"),
                                        fieldWithPath("postType").description("게시글의 종류").type("ENUM")
                                                .attributes(key("enumValues").value(List.of(PostType.values()))))
                                .responseFields(
                                        fieldWithPath("message").description("요청 결과 메세지"),
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("data.postId").description("생성된 게시글 ID"))
                                .tag("Nest")
                                .build()
                )));
    }

    @Test
    @DisplayName("둥지 - 공지글 생성 API")
    void announcement_create_test() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setProfileId(1L);
        request.setPostType(PostType.ANNOUNCEMENT.name());
        request.setContent("안녕하세요 내용이에요.");

        when(postApiService.createPost(any()))
                .thenReturn(PostResponse.of(1L));

        mockMvc.perform(
                        post("/api/v1/nest/post")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("둥지 글 생성 API 입니다.")
                                        .summary("둥지 글 생성 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("profileId").description("대상 프로필의 id"),
                                                fieldWithPath("content").description("글의 내용"),
                                                fieldWithPath("postType").description("게시글의 종류").type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(PostType.values())))
                                        )
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.postId").description("작성된 공지의 ID")
                                        )
                                        .tag("Nest")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("둥지 - 질문글 생성 API")
    void question_create_test() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setProfileId(1L);
        request.setPostType(PostType.QUESTION.name());
        request.setContent("봄날의 햇살 유승훈.");

        when(postApiService.createPost(any()))
                .thenReturn(PostResponse.of(1L));

        mockMvc.perform(
                        post("/api/v1/nest/post")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("둥지 글 생성 API 입니다.")
                                        .summary("둥지 글 생성 API 입니다.")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("유저의 JWT"))
                                        .requestFields(
                                                fieldWithPath("profileId").description("대상 프로필의 id"),
                                                fieldWithPath("content").description("글의 내용"),
                                                fieldWithPath("postType").description("게시글의 종류").type("ENUM")
                                                        .attributes(key("enumValues").value(List.of(PostType.values()))))
                                        .responseFields(
                                                fieldWithPath("message").description("요청 결과 메세지"),
                                                fieldWithPath("code").description("결과 코드"),
                                                fieldWithPath("data.postId").description("작성된 질문글의 ID")
                                        )
                                        .tag("Nest")
                                        .build()
                        )));
    }
}
