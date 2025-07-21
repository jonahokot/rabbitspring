package com.interswitch.rabbitspringconsumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "webhook")
public class WebhookProperties {
    private List<String> endpoints;
    private int retryAttempts = 3;
    private long timeoutSeconds = 30;
    private String userAgent = "rabbitspring-webhook-service";
    private boolean enableRetries = true;

}
