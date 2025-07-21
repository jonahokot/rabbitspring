package com.interswitch.rabbitspringconsumer.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
        public static final String QUEUE_NAME = "webhook-events-queue";

}
