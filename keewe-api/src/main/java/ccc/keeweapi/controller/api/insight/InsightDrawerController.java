package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.dto.insight.DrawerResponse;
import ccc.keeweapi.dto.insight.DrawerUpdateRequest;
import ccc.keeweapi.service.insight.command.InsightDrawerCommandApiService;
import ccc.keeweapi.service.insight.query.InsightDrawerQueryApiService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drawer")
@RequiredArgsConstructor
public class InsightDrawerController {

    private final InsightDrawerCommandApiService insightDrawerCommandApiService;
    private final InsightDrawerQueryApiService insightDrawerQueryApiService;

    @PostMapping
    public ApiResponse<DrawerCreateResponse> create(@RequestBody @Valid DrawerCreateRequest request) {
        return ApiResponse.ok(insightDrawerCommandApiService.create(request));
    }

    @GetMapping
    public ApiResponse<List<DrawerResponse>> getMyDrawers() {
        return ApiResponse.ok(insightDrawerQueryApiService.getMyDrawers());
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<DrawerResponse>> getDrawers(@PathVariable Long userId) {
        return ApiResponse.ok(insightDrawerQueryApiService.getDrawers(userId));
    }

    @PatchMapping("/{drawerId}")
    public ApiResponse<Void> updateDrawer(@PathVariable Long drawerId, @RequestBody DrawerUpdateRequest request) {
        insightDrawerCommandApiService.update(drawerId, request);
        return ApiResponse.ok();
    }
}
