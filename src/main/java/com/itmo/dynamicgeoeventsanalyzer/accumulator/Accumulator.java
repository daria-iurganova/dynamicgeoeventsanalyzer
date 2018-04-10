package com.itmo.dynamicgeoeventsanalyzer.accumulator;

import com.itmo.dynamicgeoeventsanalyzer.dto.Event;
import com.itmo.dynamicgeoeventsanalyzer.dto.LatLong;
import com.itmo.dynamicgeoeventsanalyzer.dto.Node;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class Accumulator {
    @Getter
    private Collection<Node<Event>> nodes = new HashSet<>();

    public final void initNodes(final Collection<Node<Event>> nodes) {
        this.nodes = nodes;
    }

    public final void addEvent(final Event event) {
        nodes.stream()
                .filter(node -> node.getSquare().contains(new LatLong(event.location.lat, event.location.lng)))
                .findFirst()
                .map(Node::getData)
                .ifPresent(data -> data.add(new Node.DataAtPoint<>(event, new LatLong(event.location.lat, event.location.lat))));
    }
}



