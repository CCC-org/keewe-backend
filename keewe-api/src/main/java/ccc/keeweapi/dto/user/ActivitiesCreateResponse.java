package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ActivitiesCreateResponse {
    private List<Activity> activities;
    private ProfileStatus status;
}
