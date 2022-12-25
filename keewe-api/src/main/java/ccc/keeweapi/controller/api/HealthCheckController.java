package ccc.keeweapi.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
public class HealthCheckController {

    @GetMapping
    public String doHealthCheck() {
        return "I'm alive.";
    }
}
