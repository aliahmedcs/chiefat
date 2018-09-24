package com.magdsoft;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.magdsoft.ws.controller.ListenerBean;

@Configuration
public class ApplicationConfig {

	@Bean
    public ListenerBean listenerBean() {
        return new ListenerBean();
    }
}