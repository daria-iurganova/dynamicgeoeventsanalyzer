package com.itmo.dynamicgeoeventsanalyzer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StringToGeojsonMapper {
    public static GeoJson map(final String content) throws IOException {
        return new ObjectMapper().readValue(content, GeoJson.class);
    }
}
