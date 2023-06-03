package ccc.keeweinfra.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class VirtualProfileResponse implements OauthResponse {
    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getEmail() {
        return null;
    }
}
