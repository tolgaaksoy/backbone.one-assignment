package one.backbone.messagingassignment.service.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateUtil {

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String ISO8601_DATE_FORMAT_WITHOUT_MILLIS = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static LocalDateTime getLocalDateTimeFromISO8601String(String date) {
        return getLocalDateTimeFromISO8601String(date, false);
    }

    public static LocalDateTime getLocalDateTimeFromISO8601String(String date, boolean millis) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            if (millis) {
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(ISO8601_DATE_FORMAT));
            } else {
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(ISO8601_DATE_FORMAT_WITHOUT_MILLIS));
            }
        } catch (Exception e) {
            log.error("Error parsing date {} with format {}", date, ISO8601_DATE_FORMAT);
            return null;
        }
    }


    public static Float getDifferenceInMinutes(LocalDateTime createdAt, LocalDateTime createdAt1) {
        return (float) (createdAt1.getMinute() - createdAt.getMinute());
    }

    public static Float getDifferenceInSeconds(LocalDateTime createdAt, LocalDateTime createdAt1) {
        return (float) (createdAt1.getSecond() - createdAt.getSecond());
    }
}
