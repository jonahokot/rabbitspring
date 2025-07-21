package com.interswitch.rabbitspringconsumer.service;

import com.interswitch.rabbitspringconsumer.config.WebhookProperties;
import com.interswitch.rabbitspringconsumer.model.WebhookEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class WebhookService {

    private final WebClient webClient = WebClient.create();
    private final WebhookProperties webhookProperties;

    public WebhookService(WebhookProperties webhookProperties) {
        this.webhookProperties = webhookProperties;
    }


    public void sendWebhook(WebhookEvent event) {
        log.info("🚀 Sending webhook event {} to {} endpoint(s)", event.getId(),
                webhookProperties.getEndpoints().size());

        webhookProperties.getEndpoints().forEach(endpoint -> {
            try {
                log.info("📤 Sending to: {}", endpoint);

                webClient.post()
                        .uri(endpoint)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header("X-Webhook-Event-Type", event.getEventType())
                        .bodyValue(event)
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(
                                response -> log.info("✅ Success: {}", endpoint),
                                error -> log.error("❌ Failed: {} - {}", endpoint, error.getMessage())
                        );

            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }
        });
    }
}