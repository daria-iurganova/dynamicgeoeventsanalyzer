package com.itmo.dynamicgeoeventsanalyzer.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class LatestMatchingPathFinder {
    private static final String YEAR_PATTERN = "yyyy";

    public static String getFileContent(final String postfix, final Date date) {
        final int year = Integer.valueOf(new SimpleDateFormat(YEAR_PATTERN).format(date));
        for (int i = 0; i < 5; i++) {
            final InputStream resourceAsStream = getInputStream(year - i + postfix );
            if (resourceAsStream != null)
                return new BufferedReader(new InputStreamReader(resourceAsStream))
                        .lines().collect(Collectors.joining("\n"));
        }
        throw new RuntimeException("Failed to find any files with provided postfix");
    }

    private static InputStream getInputStream(final String path) {
        return StringToGeojsonMapper.class.getClassLoader().getResourceAsStream(Paths.get("grids", path).toString());
    }
}
