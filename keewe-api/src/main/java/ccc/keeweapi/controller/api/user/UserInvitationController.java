package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.InviteeListResponse;
import ccc.keeweapi.dto.user.InviteeSearchResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import ccc.keewedomain.persistence.repository.user.cursor.InviteeSearchCursor;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserInvitationController {
    private final ProfileApiService profileApiService;

    @GetMapping("/invitee")
    public ApiResponse<InviteeListResponse> paginateInvitees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
            @RequestParam Long limit) {
        CursorPageable<LocalDateTime> cPage = CursorPageable.of(cursor, limit);
        return ApiResponse.ok(profileApiService.paginateInvitees(cPage));
    }

    @GetMapping("/invitee/search")
    public ApiResponse<InviteeSearchResponse> searchInvitees(
            @RequestParam String searchWord,
            @RequestParam(required = false) String cursor,
            @RequestParam Long limit
    ) {
        CursorPageable<InviteeSearchCursor> cPage = CursorPageable.of(InviteeSearchCursor.from(cursor), limit);
        return ApiResponse.ok(profileApiService.searchInvitees(searchWord, cPage));
    }
}
