package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getCurrentDate() {
        return LocalDate.now().format(DEFAULT_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DEFAULT_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DEFAULT_FORMATTER);
    }
}
