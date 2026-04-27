package diary.utils;

import java.time.*;

public class DateUtils {

    // vytvoření bezpečného času
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    // převod z kalendáře
    public static LocalDateTime fromDate(LocalDate date) {
        if (date == null) {
            return LocalDateTime.now();
        }
        return date.atStartOfDay();
    }

    // porovnání dne
    public static boolean sameDay(LocalDateTime a, LocalDate b) {
        return a.toLocalDate().equals(b);
    }
}