package com.interswitch.rabbitspringconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.interswitch.rabbitspringconsumer.config.WebhookProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebhookProperties.class)
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
