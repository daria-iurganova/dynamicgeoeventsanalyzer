package com.itmo.dynamicgeoeventsanalyzer.configuration;

import com.itmo.dynamicgeoeventsanalyzer.accumulator.Accumulator;
import com.itmo.dynamicgeoeventsanalyzer.analyzer.Analyzer;
import com.itmo.dynamicgeoeventsanalyzer.dto.Event;
import com.itmo.dynamicgeoeventsanalyzer.reader.GeojsonToNodeMapper;
import com.itmo.dynamicgeoeventsanalyzer.reader.InputSourcePathChooser;
import com.itmo.dynamicgeoeventsanalyzer.reader.LatestMatchingPathFinder;
import com.itmo.dynamicgeoeventsanalyzer.reader.StringToGeojsonMapper;
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
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableIntegration
@EnableAsync
@IntegrationComponentScan
@ComponentScan(excludeFilters = {@ComponentScan.Filter(Configuration.class)}, basePackages = "com.itmo.dynamicgeoeventsanalyzer")
public class AppConfig {
    @Setter(onMethod = @__(@Autowired))
    private Accumulator accumulator;
    @Setter(onMethod = @__(@Autowired))
    private Analyzer analyzer;

    @Bean
    public MessageChannel eventsChannel() {
        return MessageChannels.executor(eventsThreadPoolTaskExecutor()).get();
    }

    @Bean
    public Executor eventsThreadPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setQueueCapacity(10000);
        return executor;
    }

    @Bean
    public IntegrationFlow eventsFlow() {
        return IntegrationFlows.from("eventsChannel")
                .transform(new JsonToObjectTransformer(Event.class))
                .handle(accumulator, "addEvent")
                .get();
    }

    @Bean
    public MessageChannel hourSwitchInput() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow hourSwitch() {
        return IntegrationFlows.from("hourSwitchInput")
                .handle(getDumpMessageHandler())
                .get();
    }

    @Bean
    public MessageHandler getDumpMessageHandler() {
        return c -> {
            try {
                analyzer.updateNodes(accumulator.getNodes());
                accumulator.initNodes(GeojsonToNodeMapper.map(StringToGeojsonMapper.map(LatestMatchingPathFinder.getFileContent(InputSourcePathChooser.getPathPostfix(new Date()), new Date()))));
            } catch (IOException e) {
                throw new RuntimeException("Failed to reinit");
            }
        };
    }

    @Bean
    public Executor hourSwitchExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
