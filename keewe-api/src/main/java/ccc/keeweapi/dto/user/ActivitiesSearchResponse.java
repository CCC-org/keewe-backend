package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.common.enums.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesSearchResponse {
    private List<Activity> activities;
}
