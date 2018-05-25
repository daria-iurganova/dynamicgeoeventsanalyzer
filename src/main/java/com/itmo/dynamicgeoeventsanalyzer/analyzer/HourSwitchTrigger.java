package com.itmo.dynamicgeoeventsanalyzer.analyzer;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HourSwitchTrigger {
    private static final String TRIGGER = "trigger";
    private final MessageChannel hourSwitchInput;
    private String prevHour = getH();

    public void watchTime() {
        while (true) {
            if (!getH().equals(prevHour))
                hourSwitchInput.send(new GenericMessage<>(TRIGGER));
            prevHour = getH();
        }
    }

    private String getH() {
        return new SimpleDateFormat("m").format(new Date());
    }
}

