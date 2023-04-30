package ccc.keeweapi.controller.api.notification;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ccc.keeweapi.document.utils.ApiDocumentationTest;
import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keeweapi.dto.notification.PaginateNotificationResponse;
import ccc.keeweapi.service.notification.command.NotificationCommandApiService;
import ccc.keeweapi.service.notification.query.NotificationQueryApiService;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
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

public class NotificationControllerTest extends ApiDocumentationTest {
    @InjectMocks
    NotificationController notificationController;

    @Mock
    NotificationCommandApiService notificationCommandApiService;

    @Mock
    NotificationQueryApiService notificationQueryApiService;

    @BeforeEach
    public void setup(RestDocumentationContextProvider provider) {
        super.setup(notificationController, provider);
    }

    @Test
    @DisplayName("알림 현황 페이지네이션 API 테스트")
    void testPaginateNotifications() throws Exception {
        when(notificationQueryApiService.paginateNotifications(any())).thenReturn(
            PaginateNotificationResponse.of(3L,
                List.of(NotificationResponse.of(3L, "내 인사이트에 \n누군가 댓글 남김", "유승훈님이 댓글을 남겼어요.", NotificationCategory.COMMENT, "3", false, LocalDateTime.now().toLocalDate().toString())
            )
        ));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/notification")
                        .param("cursor", Long.toString(3))
                        .param("limit", Long.toString(10))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("알림 조회 API입니다.")
                        .summary("알림 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .requestParameters(
                                parameterWithName("cursor").description("nextCursor 값"),
                                parameterWithName("limit").description("가져올 알림 개수"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.nextCursor").description("다음 알림 커서 ID (null이면 마지막 페이지)"),
                                fieldWithPath("data.notifications[].id").description("알림 ID"),
                                fieldWithPath("data.notifications[].title").description("알림 제목"),
                                fieldWithPath("data.notifications[].contents").description("알림 본문"),
                                fieldWithPath("data.notifications[].category").description("알림 카테고리"),
                                fieldWithPath("data.notifications[].referenceId").description("알림 참조 ID"),
                                fieldWithPath("data.notifications[].read").description("알림 읽었는지 여부")
                        )
                        .tag("Notification")
                        .build()
        )));
    }

    @Test
    @DisplayName("알림 읽음 처리 API 테스트")
    void testMarkAsRead() throws Exception {
        when(notificationCommandApiService.markAsRead(anyLong())).thenReturn(
               NotificationResponse.of(3L, "내 인사이트에 \n누군가 댓글 남김", "유승훈님이 댓글을 남겼어요.", NotificationCategory.COMMENT, "3", false, LocalDateTime.now().toLocalDate().toString())
        );

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/notification/{notificationId}/read", 3L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andDo(restDocs.document(resource(
                ResourceSnippetParameters.builder()
                        .description("알림 읽음 처리 API입니다.")
                        .summary("알림 읽음 처리 API")
                        .requestHeaders(
                                headerWithName("Authorization").description("유저의 JWT"))
                        .responseFields(
                                fieldWithPath("message").description("요청 결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data.id").description("알림 ID"),
                                fieldWithPath("data.title").description("알림 제목"),
                                fieldWithPath("data.contents").description("알림 본문"),
                                fieldWithPath("data.category").description("알림 카테고리"),
                                fieldWithPath("data.referenceId").description("알림 참조 ID"),
                                fieldWithPath("data.read").description("알림 읽었는지 여부")
                        )
                        .tag("Notification")
                        .build()
        )));
    }
}
