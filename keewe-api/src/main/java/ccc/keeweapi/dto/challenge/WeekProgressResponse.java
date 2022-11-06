package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


@Getter
@AllArgsConstructor(staticName = "of")
public class WeekProgressResponse {

    private Long challengeId;
    private Long remain;
    private String challengeName;
    private LocalDate startDate;
    private List<DayProgressResponse> dayProgresses;
}
