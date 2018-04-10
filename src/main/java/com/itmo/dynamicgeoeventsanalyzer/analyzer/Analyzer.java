package com.itmo.dynamicgeoeventsanalyzer.analyzer;

import com.itmo.dynamicgeoeventsanalyzer.dto.Event;
import com.itmo.dynamicgeoeventsanalyzer.dto.Node;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class Analyzer {
    private Collection<Node<Event>> nodes;

    public void updateNodes(final Collection<Node<Event>> newNodes) {

    }
}
