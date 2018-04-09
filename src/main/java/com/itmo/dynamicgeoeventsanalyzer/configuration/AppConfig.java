package com.itmo.dynamicgeoeventsanalyzer.configuration;

import com.itmo.dynamicgeoeventsanalyzer.accumulator.Accumulator;
import com.itmo.dynamicgeoeventsanalyzer.eventslistener.Event;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@ComponentScan(excludeFilters = {@ComponentScan.Filter(Configuration.class)}, basePackages = "com.itmo.dynamicgeoeventsanalyzer")
public class AppConfig {
    @Setter(onMethod = @__(@Autowired))
    private Accumulator accumulator;

    //TODO: use ExecutorSubscribableChannel
    @Bean
    public MessageChannel eventsChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow eventsFlow() {
        return IntegrationFlows.from("eventsChannel")
                .transform(new JsonToObjectTransformer(Event.class))
                .handle(accumulator, "addEvent")
                .get();
    }
}
