package com.itmo.dynamicgeoeventsanalyzer;

import com.itmo.dynamicgeoeventsanalyzer.configuration.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        AbstractApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfig.class);
        context.registerShutdownHook();
        SpringApplication.run(Application.class, args);
    }
}