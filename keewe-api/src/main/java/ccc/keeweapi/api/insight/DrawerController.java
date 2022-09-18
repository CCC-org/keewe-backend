package ccc.keeweapi.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.service.insight.DrawerApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drawer")
@RequiredArgsConstructor
public class DrawerController {

    private final DrawerApiService drawerApiService;

    @PostMapping
    public ApiResponse<DrawerCreateResponse> create(@RequestBody DrawerCreateRequest request) {
        return ApiResponse.ok(drawerApiService.create(request));
    }
}
