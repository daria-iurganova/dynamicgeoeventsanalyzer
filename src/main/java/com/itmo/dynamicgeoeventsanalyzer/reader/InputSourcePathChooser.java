package com.itmo.dynamicgeoeventsanalyzer.reader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InputSourcePathChooser {
    private static final String DELIMITER = "-";
    private static final String FORMAT = ".geojson";
    private static final String WEEKDAY_PATTERN = "E";
    private static final String SUN = "Sun";
    private static final String SAT = "Sat";
    private static final String WEEKEND = "weekend";
    private static final String WORKING = "working";
    private static final String MONTH_AND_HOUR_PATTERN = "MM-H.00";

    public static String getPathPostfix(final Date date) {
        return DELIMITER + getMonthAndHour(date) + DELIMITER + getDayType(date) + FORMAT;
    }

    private static String getDayType(Date date) {
        final String weekday = new SimpleDateFormat(WEEKDAY_PATTERN, Locale.US).format(date);
        return weekday.equals(SAT) || weekday.equals(SUN) ? WEEKEND : WORKING;
    }

    private static String getMonthAndHour(Date date) {
        return new SimpleDateFormat(MONTH_AND_HOUR_PATTERN).format(date);
    }
}
