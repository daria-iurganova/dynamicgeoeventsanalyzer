package com.itmo.dynamicgeoeventsanalyzer.reader;

import com.itmo.dynamicgeoeventsanalyzer.dto.Event;
import com.itmo.dynamicgeoeventsanalyzer.dto.Node;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeojsonToNodeMapper {
    public static Collection<Node<Event>> map(final GeoJson geoJson) {
        return geoJson.getFeatures().stream()
                .map(GeoJson.Feature::getGeometry)
                .map(GeoJson.Geometry::getCoordinates)
                .map(col -> col.get(0))
                .map(col -> new Node.Square(col.get(0).get(1), col.get(2).get(1),
                        col.get(0).get(0), col.get(2).get(0)))
                .map((Function<Node.Square, Node<Event>>) Node::new)
                .collect(Collectors.toSet());
    }
}
