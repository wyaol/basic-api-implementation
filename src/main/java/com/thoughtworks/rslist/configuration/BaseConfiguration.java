package com.thoughtworks.rslist.configuration;

import com.thoughtworks.rslist.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfiguration {

    @Bean
    public EventService eventService() {
        return new EventService();
    }
}
