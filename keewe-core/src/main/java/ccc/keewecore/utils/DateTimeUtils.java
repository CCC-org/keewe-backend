package ccc.keewecore.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
    public static int getWeekCountByRange(LocalDateTime start,
                                          LocalDateTime end,
                                          LocalDateTime now) {
        long daysDiff = ChronoUnit.DAYS.between(start.toLocalDate(), now.toLocalDate());
        int weekNumber = (int) (daysDiff / 7) + 1;
        if (now.isBefore(end)) {
            long remainingDays = ChronoUnit.DAYS.between(now.toLocalDate(), end.toLocalDate());
            if (remainingDays >= 0 && remainingDays < 7) {
                weekNumber++;
            }
        }
        return weekNumber;
    }
}
