package com.itmo.dynamicgeoeventsanalyzer.reader;

import lombok.Value;

import java.util.Collection;
import java.util.List;

@Value
public class GeoJson {
    String type;
    Collection<Feature> features;

    @Value
    public static class Feature {
        String type;
        Properties properties;
        Geometry geometry;
    }

    @Value
    public static class Properties {
        Long average_value;
        String day_type;
        Integer month;
        Integer hour;
    }

    @Value
    public static class Geometry {
        String type;
        List<List<List<Double>>> coordinates;
    }
}
