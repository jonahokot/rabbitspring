package com.interswitch.rabbitspringconsumer.service;

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

    // Hardcoded endpoints - replace with your webhook URLs
    private final List<String> endpoints = Arrays.asList(
            "https://webhook.site/4a4a5515-4335-49d7-a3ee-1b047748940d",
            "https://httpbin.org/post"
    );

    public void sendWebhook(WebhookEvent event) {
        log.info("🚀 Sending webhook event {} to {} endpoint(s)", event.getId(), endpoints.size());

        endpoints.forEach(endpoint -> {
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