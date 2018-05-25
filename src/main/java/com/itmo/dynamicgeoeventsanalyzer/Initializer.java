package com.itmo.dynamicgeoeventsanalyzer;

import com.itmo.dynamicgeoeventsanalyzer.accumulator.Accumulator;
import com.itmo.dynamicgeoeventsanalyzer.analyzer.HourSwitchTrigger;
import com.itmo.dynamicgeoeventsanalyzer.dto.Event;
import com.itmo.dynamicgeoeventsanalyzer.dto.LatLong;
import com.itmo.dynamicgeoeventsanalyzer.dto.Node;
import com.itmo.dynamicgeoeventsanalyzer.reader.GeojsonToNodeMapper;
import com.itmo.dynamicgeoeventsanalyzer.reader.InputSourcePathChooser;
import com.itmo.dynamicgeoeventsanalyzer.reader.LatestMatchingPathFinder;
import com.itmo.dynamicgeoeventsanalyzer.reader.StringToGeojsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class Initializer {
    private static final String EVENTS_CHANNEL = "eventsChannel";

    @Value("${events.provider.host}")
    private String eventsProviderHost;

    private final IntegrationFlowContext flowContext;
    private final Accumulator accumulator;
    private final HourSwitchTrigger hourSwitchTrigger;
    private final Executor hourSwitchExecutor;

    @EventListener(ContextRefreshedEvent.class)
    private void init() throws IOException {
        final Collection<Node<Event>> nodes = getNodes(new Date());
        accumulator.initNodes(nodes);
        nodes.stream()
                .map(Node::getSquare)
                .map(s -> getMessageProducer(s.getCenter()))
                .collect(Collectors.toSet())
                .forEach(this::registerFlow);
        hourSwitchExecutor.execute(hourSwitchTrigger::watchTime);
    }

    private Collection<Node<Event>> getNodes(Date date) throws IOException {
        return GeojsonToNodeMapper.map(StringToGeojsonMapper.map(LatestMatchingPathFinder.getFileContent(InputSourcePathChooser.getPathPostfix(date), date)));
    }

    private void registerFlow(final MessageProducerSupport messageProducer) {
        flowContext.registration(IntegrationFlows.from(messageProducer).channel(EVENTS_CHANNEL)
                .get()).register();
    }

    private MessageProducerSupport getMessageProducer(LatLong point) {
        eventsProviderHost = "localhost:4567";
        return webSocketInboundChannelAdapter("ws://" + eventsProviderHost + "/feed?lat=" + point.getLatitude() + "&lon=" + point.getLongitude());
    }

    private MessageProducerSupport webSocketInboundChannelAdapter(final String uri) {
        return new WebSocketInboundChannelAdapter(new ClientWebSocketContainer(new StandardWebSocketClient(), uri));
    }
}
