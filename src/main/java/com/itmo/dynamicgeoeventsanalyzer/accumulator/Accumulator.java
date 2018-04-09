package com.itmo.dynamicgeoeventsanalyzer.accumulator;

import com.itmo.dynamicgeoeventsanalyzer.eventslistener.Event;
import com.itmo.dynamicgeoeventsanalyzer.quadtree.Node;
import org.springframework.stereotype.Component;

@Component
public class Accumulator {
    public final Node<String> node = new Node<>();

    public final void addEvent(final Event event) {
        throw new RuntimeException("YAy");
    }
}

