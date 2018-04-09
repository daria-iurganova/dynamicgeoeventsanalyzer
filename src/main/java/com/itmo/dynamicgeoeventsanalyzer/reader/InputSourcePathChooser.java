package com.itmo.dynamicgeoeventsanalyzer.reader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InputSourcePathChooser {
    private static final String DELIMITER = "-";
    private static final String FORMAT = ".geojson";

    public static String getPathPostfix(final Date date) {
        return DELIMITER + getMonthAndHour(date) + DELIMITER + getDayType(date) + FORMAT;
    }

    private static String getDayType(Date date) {
        final String weekday = new SimpleDateFormat("E", Locale.US).format(date);
        return weekday.equals("Sat") || weekday.equals("Sun") ? "weekend" : "working";
    }

    private static String getMonthAndHour(Date date) {
        return new SimpleDateFormat("MM-H.00").format(date);
    }
}
