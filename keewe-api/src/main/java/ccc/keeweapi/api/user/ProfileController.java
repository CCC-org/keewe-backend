package ccc.keeweapi.api.user;

import ccc.keeweapi.dto.user.CreateLinkDto;
import ccc.keeweapi.service.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/link")
    public Long createLink() {
        return profileService.createLink(new CreateLinkDto(4L, "hs"));
    }
}
