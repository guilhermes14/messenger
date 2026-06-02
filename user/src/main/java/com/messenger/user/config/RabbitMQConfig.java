package com.messenger.user.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public JacksonJsonMessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
        //ObjectMapper objectMapper = new ObjectMapper();
        //return new JacksonJsonMessageConverter(objectMapper);
    }
}
