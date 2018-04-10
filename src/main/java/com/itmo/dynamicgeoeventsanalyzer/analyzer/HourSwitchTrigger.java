package com.itmo.dynamicgeoeventsanalyzer.analyzer;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.awaitility.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.awaitility.Awaitility.await;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HourSwitchTrigger {
    private final MessageChannel hourSwitchInput;
    private String prevHour = getH();

    public void watchTime() {
        while (true) {
            if (!getH().equals(prevHour))
                hourSwitchInput.send(new GenericMessage<>("trigger"));
            prevHour = getH();
        }
    }

    private String getH() {
        return new SimpleDateFormat("m").format(new Date());
    }
}

