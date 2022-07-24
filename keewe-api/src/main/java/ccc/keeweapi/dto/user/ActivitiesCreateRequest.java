package ccc.keeweapi.dto.user;

import ccc.keeweapi.validation.Enum;
import ccc.keewedomain.domain.common.enums.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesCreateRequest {
    private Long profileId;
    @Enum(enumClass = Activity.class)
    private List<String> activities;
}
