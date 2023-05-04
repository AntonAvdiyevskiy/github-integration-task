package com.example.vcs.config;

import com.example.vcs.service.handler.CustomClientExceptionHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder
                .errorHandler(new CustomClientExceptionHandler())
                .build();
    }
}
