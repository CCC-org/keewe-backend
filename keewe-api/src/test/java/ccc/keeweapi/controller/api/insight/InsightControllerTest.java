package ccc.keeweapi.controller.api.insight;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.insight.response.*;
import ccc.keeweapi.service.insight.command.InsightCommandApiService;
import ccc.keeweapi.service.insight.query.InsightQueryApiService;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.util.List;
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

public class InsightControllerTest extends ApiDocumentationTest {

    @InjectMocks
    InsightController insightController;

    @Mock
    InsightQueryApiService insightQueryApiService;

    @Mock
    InsightCommandApiService insightCommandApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(insightController, provider);
    }

    @Test
    @DisplayName("인사이트 등록 API")
    void create_insight_test() throws Exception {
        String contents = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용";
        String link = "https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/";
        boolean participation = false;
        Long drawerId = 1L;

        JSONObject insightCreateRequest = new JSONObject();
        insightCreateRequest
                .put("contents", contents)
                .put("link", link)
                .put("participation", participation)
                .put("drawerId", drawerId);

        when(insightCommandApiService.create(any())).thenReturn(InsightCreateResponse.of(1L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/insight")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(insightCreateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 생성 API 입니다.")
                        .summary("인사이트 생성 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("contents").description("인사이트의 내용. 최대 300자, 문자 제한 없음"),
                                fieldWithPath("link").description("등록한 링크의 link. 최대 2000자"),
                                fieldWithPath("participation").description("현재 진행중인 챌린지에 참가할 지 여부"),
                                fieldWithPath("drawerId").description("서랍의 ID. 선택").optional())
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightId").description("생성된 인사이트의 ID"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 수정 API")
    void updateInsight() throws Exception {
        String contents = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용";
        String link = "https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/";

        JSONObject insightUpdateRequest = new JSONObject();
        insightUpdateRequest
                .put("insightId", 3L)
                .put("contents", contents)
                .put("link", link);

        when(insightCommandApiService.update(any())).thenReturn(InsightUpdateResponse.of(1L));

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/insight")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .content(insightUpdateRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 수정 API 입니다.")
                        .summary("인사이트 수정 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestFields(
                                fieldWithPath("insightId").description("인사이트 ID"),
                                fieldWithPath("contents").description("인사이트의 내용. 최대 300자, 문자 제한 없음"),
                                fieldWithPath("link").description("등록한 링크의 link. 최대 2000자"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightId").description("수정된 인사이트의 ID"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 조회 API")
    void get_insight_test() throws Exception {
        Long insightId = 1L;

        when(insightQueryApiService.getInsight(insightId)).thenReturn(InsightGetResponse.of(
                insightId,
                "인사이트 내용입니다. 즐거운 개발 되세요!",
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                true
                )
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 조회 API 입니다.")
                        .summary("인사이트 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.id").description("인사이트 ID"),
                                fieldWithPath("data.contents").description("인사이트 내용"),
                                fieldWithPath("data.link.url").description("인사이트 링크"),
                                fieldWithPath("data.reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data.reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data.reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data.reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data.reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data.reaction.eyes").description("인사이트 눈 반응 수"),
                                fieldWithPath("data.bookmark").description("인사이트 북마크 여부"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 삭제 API")
    void delete_insight_test() throws Exception {
        Long insightId = 1L;

        when(insightCommandApiService.delete(insightId))
                .thenReturn(InsightDeleteResponse.of(insightId));

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/insight/{insightId}", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 삭제 API 입니다.")
                        .summary("인사이트 삭제 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT")
                        )
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.insightId").description("삭제 인사이트 ID"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("홈 인사이트 조회 API")
    void get_insight_for_home_test() throws Exception {
        Long insightId = 1L;
        long cursor = Long.MAX_VALUE;
        long limit = 10L;

        when(insightQueryApiService.getInsightsForHome(any(), any())).thenReturn(List.of(InsightGetForHomeResponse.of(
                insightId,
                "인사이트 내용입니다. 즐거운 개발 되세요!",
                true,
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().toString(),
                InsightWriterDto.of(1L, "nickname", "title", "image")
        )));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("follow", "false")
                        .param("cursor", Long.toString(cursor))
                        .param("limit", Long.toString(limit)))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("홈 인사이트 조회 API 입니다.")
                        .summary("홈 인사이트 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("follow").description("팔로우 필터 여부").optional(),
                                parameterWithName("cursor").description("마지막으로 받은 인사이트 ID"),
                                parameterWithName("limit").description("가져올 인사이트 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("인사이트 ID"),
                                fieldWithPath("data[].contents").description("인사이트 내용"),
                                fieldWithPath("data[].bookmark").description("인사이트 북마크 여부"),
                                fieldWithPath("data[].link.url").description("인사이트 링크"),
                                fieldWithPath("data[].reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data[].reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data[].reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data[].reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data[].reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data[].reaction.eyes").description("인사이트 눈 반응 수"),
                                fieldWithPath("data[].createdAt").description("인사이트 생성 시간"),
                                fieldWithPath("data[].writer.writerId").description("인사이트 저자 ID"),
                                fieldWithPath("data[].writer.nickname").description("인사이트 저자 닉네임"),
                                fieldWithPath("data[].writer.title").description("인사이트 저자 타이틀"),
                                fieldWithPath("data[].writer.image").description("인사이트 저자 사진"))
                        .tag("Insight")
                        .build()
        )));
    }


    @Test
    @DisplayName("인사이트 조회수 증가 API")
    void increment_insight_views() throws Exception {

        when(insightCommandApiService.incrementViewCount(anyLong())).thenReturn(InsightViewIncrementResponse.of(123L));

        ResultActions resultActions = mockMvc.perform(post("/api/v1/insight/view/{insightId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 조회수 증가 API 입니다.")
                        .summary("인사이트 조회수 증가 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.viewCount").description("조회수"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 작성자 프로필 조회 API")
    void insight_author_get() throws Exception {

        when(insightQueryApiService.getInsightAuthorAreaInfo(anyLong())).thenReturn(
                InsightAuthorAreaResponse.of(1L,
                        "nickname",
                        "고급 기록가",
                        List.of(Interest.of("운동"), Interest.of("영화")),
                        "www.api-keewe.com/images/128398681",
                        true,
                        true,
                        LocalDateTime.now().toString()
                )
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/author/{insightId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 상세 조회 작성자 프로필 조회 API 입니다.")
                        .summary("인사이트 작성자 프로필 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.authorId").description("작성자 ID (유저 ID)"),
                                fieldWithPath("data.nickname").description("작성자 닉네임"),
                                fieldWithPath("data.title").description("작성자 대표 타이틀 (업적)"),
                                fieldWithPath("data.interests[].name").description("작성자 관심사 목록"),
                                fieldWithPath("data.image").description("작성자 프로필 이미지 링크"),
                                fieldWithPath("data.author").description("인사이트 조회자와 작성자 일치 여부"),
                                fieldWithPath("data.following").description("인사이트 작성자 팔로잉 여부 (작성자와 조회자가 같을 경우 항상 false)"),
                                fieldWithPath("data.createdAt").description("작성자 닉네임"))
                        .tag("Insight")
                        .build()
        )));

    }

    @Test
    @DisplayName("인사이트의 챌린지 기록 조회 API")
    void insight_challenge_record() throws Exception {

        when(insightQueryApiService.getChallengeRecord(anyLong())).thenReturn(
                ChallengeRecordResponse.of(1L, "챌린지 이름", 1L, 12L)
        );

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}/challenge-record", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 챌린지 기록 조회 API 입니다.")
                        .summary("인사이트 챌린지 기록 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.challengeId").description("챌린지의 ID"),
                                fieldWithPath("data.challengeName").description("챌린지 이름"),
                                fieldWithPath("data.order").description("기록된 순서(몇 번째 기록인지)"),
                                fieldWithPath("data.total").description("기록해야 하는 인사이트의 총 개수"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("마이페이지 인사이트 조회 API")
    void get_insight_my_page_test() throws Exception {
        Long targetUserId = 1L;
        Long drawerId = 5L;
        CursorPageable<Long> cPage = CursorPageable.of(15L, 3L);

        InsightMyPageResponse insight1 = InsightMyPageResponse.of(
                30L,
                "세 번째 인사이트 내용입니다. 즐거운 개발 되세요!",
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().toString(),
                true
        );

        InsightMyPageResponse insight2 = InsightMyPageResponse.of(
                24L,
                "두 번째 인사이트 내용입니다. 즐거운 개발 되세요!",
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().minusDays(1L).toString(),
                true
        );

        InsightMyPageResponse insight3 = InsightMyPageResponse.of(
                7L,
                "첫 번째 인사이트 내용입니다. 즐거운 개발 되세요!",
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().minusDays(2L).toString(),
                true
        );




        when(insightQueryApiService.getInsightsForMyPage(any(), any(), any()))
                .thenReturn(List.of(insight1, insight2, insight3));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/my-page/{userId}", targetUserId)
                        .param("cursor", String.valueOf(cPage.getCursor()))
                        .param("limit", String.valueOf(cPage.getLimit()))
                        .param("drawerId", String.valueOf(drawerId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("마이페이지 인사이트 조회 API 입니다. 파라미터 optional 표시 잘 봐주세요")
                        .summary("마이페이지 인사이트 조회 API")
                        .pathParameters(
                                parameterWithName("userId").description("대상 유저의 ID"))
                        .requestParameters(
                                parameterWithName("cursor").description("대상 인사이트의 ID(첫 조회시 비우세요").optional(),
                                parameterWithName("limit").description("가져올 인사이트의 개수"),
                                parameterWithName("drawerId").description("폴더의 ID(전체 조회 시 비우세요)").optional())
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("인사이트 ID"),
                                fieldWithPath("data[].contents").description("인사이트 내용"),
                                fieldWithPath("data[].link.url").description("인사이트 링크"),
                                fieldWithPath("data[].createdAt").description("인사이트 생성 시각"),
                                fieldWithPath("data[].reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data[].reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data[].reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data[].reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data[].reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data[].reaction.eyes").description("인사이트 눈 반응 수"),
                                fieldWithPath("data[].bookmark").description("인사이트 북마크 여부"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("북마크한 인사이트 조회 API")
    void get_insight_for_bookmark() throws Exception {
        Long insightId = 1L;
        CursorPageable<LocalDateTime> cPage = CursorPageable.of(LocalDateTime.now(), 3L);

        when(insightQueryApiService.getInsightForBookmark(any())).thenReturn(List.of(InsightGetForBookmarkedResponse.of(
                insightId,
                "인사이트 내용입니다. 즐거운 개발 되세요!",
                true,
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().toString(),
                LocalDateTime.now().minusDays(3).toString(),
                InsightWriterDto.of(1L, "nickname", "title", "image")
        )));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/bookmark", insightId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("cursor", String.valueOf(cPage.getCursor()))
                        .param("limit", String.valueOf(cPage.getLimit())))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("북마크 인사이트 조회 API 입니다.")
                        .summary("북마크한 인사이트 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("페이징을 위한 커서 yyyy-MM-dd'T'hh:mm:ss.SSS"),
                                parameterWithName("limit").description("한번에 조회할 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("인사이트 ID"),
                                fieldWithPath("data[].contents").description("인사이트 내용"),
                                fieldWithPath("data[].bookmark").description("인사이트 북마크 여부"),
                                fieldWithPath("data[].link.url").description("인사이트 링크"),
                                fieldWithPath("data[].reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data[].reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data[].reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data[].reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data[].reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data[].reaction.eyes").description("인사이트 눈 반응 수"),
                                fieldWithPath("data[].createdAt").description("인사이트 생성 시간"),
                                fieldWithPath("data[].bookmarkedAt").description("인사이트 북마크 시간"),
                                fieldWithPath("data[].writer.writerId").description("인사이트 저자 ID"),
                                fieldWithPath("data[].writer.nickname").description("인사이트 저자 닉네임"),
                                fieldWithPath("data[].writer.title").description("인사이트 저자 타이틀"),
                                fieldWithPath("data[].writer.image").description("인사이트 저자 사진"))
                        .tag("Insight")
                        .build()
        )));
    }


    @Test
    @DisplayName("나의 챌린지 전체 기록(인사이트) 조회 API")
    void get_insights_of_my_challenge() throws Exception {
        long cursor = Long.MAX_VALUE;
        long limit = 10L;

        when(insightQueryApiService.paginateInsightsOfChallenge(any(), any())).thenReturn(List.of(InsightGetForHomeResponse.of(
                1L,
                "인사이트 내용입니다. 즐거운 개발 되세요!",
                true,
                Link.of("www.keewe.com"),
                ReactionAggregationResponse.of(1L, 2L, 3L, 4L, 5L, 6L),
                LocalDateTime.now().toString(),
                InsightWriterDto.of(1L, "nickname", "title", "image")
        )));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/challenge/my")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .param("cursor", Long.toString(cursor))
                        .param("limit", Long.toString(limit))
                        .param("writerId", Long.toString(1L)))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("나의 챌린지 전체 기록(인사이트) 조회 API 입니다.")
                        .summary("나의 챌린지 전체 기록(인사이트) 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("마지막으로 받은 인사이트 ID"),
                                parameterWithName("limit").description("가져올 인사이트 개수"),
                                parameterWithName("writerId").optional().description("인사이트 작성자 필터링. 미포함 시 전체 조회"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data[].id").description("인사이트 ID"),
                                fieldWithPath("data[].contents").description("인사이트 내용"),
                                fieldWithPath("data[].bookmark").description("인사이트 북마크 여부"),
                                fieldWithPath("data[].link.url").description("인사이트 링크"),
                                fieldWithPath("data[].reaction.clap").description("인사이트 박수 반응 수"),
                                fieldWithPath("data[].reaction.heart").description("인사이트 하트 반응 수"),
                                fieldWithPath("data[].reaction.sad").description("인사이트 슬픔 반응 수"),
                                fieldWithPath("data[].reaction.surprise").description("인사이트 놀람 반응 수"),
                                fieldWithPath("data[].reaction.fire").description("인사이트 불 반응 수"),
                                fieldWithPath("data[].reaction.eyes").description("인사이트 눈 반응 수"),
                                fieldWithPath("data[].createdAt").description("인사이트 생성 시간"),
                                fieldWithPath("data[].writer.writerId").description("인사이트 저자 ID"),
                                fieldWithPath("data[].writer.nickname").description("인사이트 저자 닉네임"),
                                fieldWithPath("data[].writer.title").description("인사이트 저자 타이틀"),
                                fieldWithPath("data[].writer.image").description("인사이트 저자 사진"))
                        .tag("Insight")
                        .build()
        )));
    }

    @Test
    @DisplayName("인사이트 통계 조회 API")
    void get_insight_statistics() throws Exception {
        when(insightQueryApiService.getStatistics(anyLong())).thenReturn(
                InsightStatisticsResponse.of(1000L, 2000L, 3000L, 40L, 50000L)
        );
        ResultActions resultActions = mockMvc.perform(get("/api/v1/insight/{insightId}/statistics", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("인사이트 통계 조회 API 입니다.")
                        .summary("인사이트 통계 조회 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.viewCount").description("인사이트의 조회 수"),
                                fieldWithPath("data.reactionCount").description("인사이트의 리액션 수 총합"),
                                fieldWithPath("data.commentCount").description("인사이트의 댓글 수"),
                                fieldWithPath("data.bookmarkCount").description("인사이트의 북마크 수"),
                                fieldWithPath("data.shareCount").description("인사이트의 공유 수"))
                        .tag("Insight")
                        .build()
        )));
    }
}
