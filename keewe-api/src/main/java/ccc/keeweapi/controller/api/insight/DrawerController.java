package ccc.keeweapi.controller.api.insight;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.dto.insight.DrawerResponse;
import ccc.keeweapi.service.insight.DrawerApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drawer")
@RequiredArgsConstructor
public class DrawerController {

    private final DrawerApiService drawerApiService;

    @PostMapping
    public ApiResponse<DrawerCreateResponse> create(@RequestBody @Valid DrawerCreateRequest request) {
        return ApiResponse.ok(drawerApiService.create(request));
    }

    @GetMapping
    public ApiResponse<List<DrawerResponse>> getMyDrawers() {
        return ApiResponse.ok(drawerApiService.getMyDrawers());
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<DrawerResponse>> getDrawers(@PathVariable Long userId) {
        return ApiResponse.ok(drawerApiService.getDrawers(userId));
    }
}
