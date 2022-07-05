package ccc.keeweapi.api.user;

import ccc.keeweapi.dto.user.CreateLinkDto;
import ccc.keeweapi.dto.user.NicknameCreateDto;
import ccc.keeweapi.service.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/link")
    public Long createLink() {
        return profileService.createLink(new CreateLinkDto(4L, "hs"));
    }

    @PostMapping("/nickname")
    public int createNickname(@RequestBody NicknameCreateDto nicknameCreateDto) {
        profileService.createNickname(nicknameCreateDto);
        return nicknameCreateDto.getNickname().length();
    }
}
