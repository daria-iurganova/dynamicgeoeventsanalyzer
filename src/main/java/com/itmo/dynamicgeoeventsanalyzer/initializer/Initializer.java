package com.itmo.dynamicgeoeventsanalyzer.initializer;

import com.itmo.dynamicgeoeventsanalyzer.quadtree.Node;
import com.itmo.dynamicgeoeventsanalyzer.reader.GeojsonToNodeMapper;
import com.itmo.dynamicgeoeventsanalyzer.reader.InputSourcePathChooser;
import com.itmo.dynamicgeoeventsanalyzer.reader.LatestMatchingPathFinder;
import com.itmo.dynamicgeoeventsanalyzer.reader.StringToGeojsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class Initializer {
    private final IntegrationFlowContext flowContext;

    @EventListener(ContextRefreshedEvent.class)
    private void init() throws IOException {
        getNodes(new Date()).stream()
                .map(Node::getSquare)
                .map(s -> getMessageProducer(s.getMinLat(), s.getMinLong()))
                .forEach(this::registerFlow);
    }

    private Collection<Node> getNodes(Date date) throws IOException {
        return GeojsonToNodeMapper.map(StringToGeojsonMapper.map(LatestMatchingPathFinder.getFileContent(InputSourcePathChooser.getPathPostfix(date), date)));
    }

    private void registerFlow(final MessageProducerSupport messageProducer) {
        flowContext.registration(IntegrationFlows.from(messageProducer).channel("eventsChannel")
                .get()).register();
    }

    private MessageProducerSupport getMessageProducer(double lat, double lon) {
        return webSocketInboundChannelAdapter("ws://localhost:4567/feed?lat=" + lon + "&lon=" + lat);
    }

    private MessageProducerSupport webSocketInboundChannelAdapter(final String uri) {
        return new WebSocketInboundChannelAdapter(new ClientWebSocketContainer(new StandardWebSocketClient(), uri));
    }
}
