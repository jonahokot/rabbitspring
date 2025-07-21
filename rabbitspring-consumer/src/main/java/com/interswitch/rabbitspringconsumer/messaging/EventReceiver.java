package com.interswitch.rabbitspringconsumer.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.rabbitspringconsumer.model.WebhookEvent;
import com.interswitch.rabbitspringconsumer.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventReceiver {

    private WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public EventReceiver(WebhookService webhookService, ObjectMapper objectMapper) {
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "webhook-events-queue")
    public void receiveWebhookEvents(String message) {
        try {
            log.info("WEBHOOK SERVICE: Received event from queue");
            log.debug("Raw message: {}", message);

            // Parse JSON message into WebhookEvent
            WebhookEvent event = objectMapper.readValue(message, WebhookEvent.class);
            log.info("Parsed event [{}] - ID: {} | Amount: {} {}",
                    event.getEventType(), event.getId(), event.getAmount(), event.getCurrency());

            // Send to configured webhook endpoints
            webhookService.sendWebhook(event);

        } catch (Exception e) {
            log.error("Error processing webhook event: {}", message, e);

        }
    }
}
