package com.anthonylimanto.restaurant_deals_task.helper;

import com.anthonylimanto.restaurant_deals_task.exception.InvalidTimeOfDayException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class TimeHelper {

    private static final DateTimeFormatter TWELVE_HOUR = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("h:mma")
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter TWENTY_FOUR_HOUR = DateTimeFormatter.ofPattern("H:mm");

    public static LocalTime parseTime(String text) {
        String trimmed = text.trim();
        try {
            return LocalTime.parse(trimmed, TWELVE_HOUR);
        } catch (DateTimeParseException notTwelveHour) {
            try {
                return LocalTime.parse(trimmed, TWENTY_FOUR_HOUR);
            } catch (DateTimeParseException notTwentyFourHour) {
                throw new InvalidTimeOfDayException(text);
            }
        }
    }
}
