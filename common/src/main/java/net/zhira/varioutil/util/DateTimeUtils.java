package net.zhira.varioutil.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtils {
    public static final long ONE_MINUTE = TimeUnit.MINUTES.toMillis(1);
    public static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    public static final long FOUR_HOURS = ONE_HOUR * 5;
    public static final long ONE_DAY = TimeUnit.DAYS.toMillis(1);
    public static final long TWO_DAYS = ONE_DAY * 2;
    public static final long WEEK = ONE_DAY * 7;

    public static final String[] DATE_PATTERNS = {"d/M/yyyy", "d MMMM yyyy", "d/M/yy", "d MMM yyyy", "yyyy MMM d", "yyyy MMMM d", "yyyy/M/d", "yyyy-M-d", "d M yyyy", "d.M.yyyy", "yyyy.m.d"};
    public static final String[] DATETIME_PATTERNS = {"d/M/yyyy-H:mm", "d MMMM yyyy H:mm", "d/M/yy H:mm", "d MMM yyyy H:mm", "yyyy MMM d H:mm", "yyyy MMMM d H:mm", "yyyy/M/d-H:mm", "yyyy-M-d-H:mm", "d M yyyy H:mm", "d.M.yyyy H:mm", "yyyy.m.d H:mm"};

    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static int getAge(long millis) {
        return getAge(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static LocalDate getLocalDate(String input, Locale locale, String... patterns) {
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Invalid date input: " + input);
    }

    public static LocalDate getLocalDate(String input, String... patterns) {
        return getLocalDate(input, Locale.getDefault(), patterns);
    }


    public static LocalDateTime getLocalDateTime(String input, Locale locale, String... patterns) {
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Invalid date input: " + input);
    }

    public static LocalDateTime getLocalDateTime(String input, String... patterns) {
        return getLocalDateTime(input, Locale.getDefault(), patterns);
    }

    public static long toMillis(String str, TimeUnit unit) {
        long total = 0;
        Matcher m = Pattern.compile("([0-9]+)(?i)(ms|[a-z])?").matcher(str);
        while (m.find()) {
            int num = Integer.parseInt(m.group(1));
            String type = String.valueOf(m.group(2));
            if (type.equalsIgnoreCase("ms")) {
                total += num;
            } else {
                switch (type.charAt(0)) {
                    case 't': case 'T': {
                        total += (num / 20.0) * 1000L;
                        break;
                    }
                    case 's': case 'S': {
                        total += TimeUnit.SECONDS.toMillis(num);
                        break;
                    }
                    case 'm': {
                        total += TimeUnit.MINUTES.toMillis(num);
                        break;
                    }
                    case 'h': case 'H': {
                        total += TimeUnit.HOURS.toMillis(num);
                        break;
                    }
                    case 'd': case 'D': {
                        total += TimeUnit.DAYS.toMillis(num);
                        break;
                    }
                    case 'w': case 'W': {
                        total += TimeUnit.DAYS.toMillis(num) * 7;
                        break;
                    }
                    case 'M': {
                        total += TimeUnit.DAYS.toMillis(num) * 31;
                        break;
                    }
                    case 'y': case 'Y': {
                        total += TimeUnit.DAYS.toMillis(num) * 365;
                        break;
                    }
                    default: {
                        if (unit == null) {
                            break;
                        }
                        switch (unit) {
                            case MILLISECONDS: total += num;
                                break;
                            case MINUTES: total += TimeUnit.MINUTES.toMillis(num);
                                break;
                            case HOURS: total += TimeUnit.HOURS.toMillis(num);
                                break;
                            case DAYS: total += TimeUnit.DAYS.toMillis(num);
                                break;
                            default: total += TimeUnit.SECONDS.toMillis(num);
                                break;
                        }
                        break;
                    }
                }
            }
        }
        return total;
    }

    public static long toMillis(String str) {
        return toMillis(str, TimeUnit.SECONDS);
    }

    public static int toTicks(String str, TimeUnit unit) {
        return (int) (toMillis(str, unit) / 1000.0 * 20);
    }

    public static int toTicks(String str) {
        return toTicks(str, TimeUnit.SECONDS);
    }

    public static ZonedDateTime getZonedDateTime(ZoneId zoneId, long millis) {
        if (ZoneId.systemDefault().equals(zoneId)) {
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId).atZone(ZoneId.systemDefault());
    }
}
