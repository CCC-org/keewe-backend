package ccc.keeweapi.controller.api.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.user.AchievedTitleResponse;
import ccc.keeweapi.dto.user.AllAchievedTitleResponse;
import ccc.keeweapi.dto.user.MyPageTitleResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
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

public class UserTitleControllerTest extends ApiDocumentationTest {
    @InjectMocks
    private UserTitleController userTitleController;

    @Mock
    private ProfileApiService profileApiService;

    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        super.setup(userTitleController, provider);
    }

    @Test
    @DisplayName("최근 획득 타이틀 3개 조회")
    void my_page_titles() throws Exception {
        Long userId = 1L;
        Long total = 10L;
        LocalDateTime now = LocalDateTime.now();
        List<AchievedTitleResponse> achievedTitleResponses = List.of(
                AchievedTitleResponse.of(1000L, "시작이 반", "회원가입 시", now),
                AchievedTitleResponse.of(2000L, "위대한 첫 도약", "첫 인사이트 업로드", now.minusDays(1)),
                AchievedTitleResponse.of(2001L, "초보 기록가", "인사이트 5개", now.minusDays(3).minusMinutes(40))
        );
        MyPageTitleResponse response = MyPageTitleResponse.of(total, achievedTitleResponses);

        when(profileApiService.getMyPageTitles(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/achieved-title/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("최근 획득 타이틀 3개 조회 API 입니다.")
                        .summary("최근 획득 타이틀 3개 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.total").description("획득한 타이틀 총 개수"),
                                fieldWithPath("data.achievedTitles[].titleId").description("타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].name").description("타이틀의 이름"),
                                fieldWithPath("data.achievedTitles[].introduction").description("타이틀 소개"),
                                fieldWithPath("data.achievedTitles[].achievedDate").description("타이틀 획득 시각"))
                        .tag("UserTitle")
                        .build()
        )));
    }

    @Test
    @DisplayName("획득 타이틀 전체 조회")
    void all_achieved_titles() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        List<AchievedTitleResponse> achievedTitleResponses = List.of(
                AchievedTitleResponse.of(1000L, "시작이 반", "회원가입 시", now),
                AchievedTitleResponse.of(2000L, "위대한 첫 도약", "첫 인사이트 업로드", now.minusDays(1)),
                AchievedTitleResponse.of(2001L, "초보 기록가", "인사이트 5개", now.minusDays(3).minusMinutes(40))
        );

        AllAchievedTitleResponse response = AllAchievedTitleResponse.of(1000L, achievedTitleResponses);

        when(profileApiService.getAllAchievedTitles(anyLong()))
                .thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/profile/all-achieved-title/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("획득 타이틀 전체 조회 API 입니다.")
                        .summary("획득 타이틀 전체 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.repTitleId").description("대표 타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].titleId").description("타이틀의 ID"),
                                fieldWithPath("data.achievedTitles[].name").description("타이틀의 이름"),
                                fieldWithPath("data.achievedTitles[].introduction").description("타이틀 소개"),
                                fieldWithPath("data.achievedTitles[].achievedDate").description("타이틀 획득 시각"))
                        .tag("UserTitle")
                        .build()
        )));
    }
}
