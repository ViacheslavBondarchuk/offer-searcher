package io.github.viacheslavbondarchuk.offersearcher.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtil {
    public static final DateFormat ISO_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private DateTimeUtil() {}

    public static String format(long millis, DateFormat dateFormat) {
        return dateFormat.format(new Date(millis));
    }

}
