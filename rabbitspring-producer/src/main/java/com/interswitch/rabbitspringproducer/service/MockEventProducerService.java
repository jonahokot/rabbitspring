package com.interswitch.rabbitspringproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.rabbitspringproducer.config.RabbitConfig;
import com.interswitch.rabbitspringproducer.model.WebhookEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class MockEventProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final String[] EVENT_TYPES = {
            "payment.created", "payment.completed", "payment.failed",
            "user.registered", "user.updated", "user.deleted",
            "order.placed", "order.shipped", "order.cancelled",
            "transaction.initiated", "refund.processed", "subscription.renewed"
    };

    private static final String[] CURRENCY = {"UGX", "KES", "USD"};
    private static final String[] STATUS = {"pending", "completed", "failed", "processing", "cancelled"};
    private static final String[] SOURCE = {"web", "mobile", "api"};


    @Scheduled(fixedDelay = 70000)
    public void generateMockEvent() {
        try {
            WebhookEvent mockEvent = createRandomMockEvent();
            String eventJson = objectMapper.writeValueAsString(mockEvent);

            String routingKey = "webhook.event." + mockEvent.getEventType().replace(".", "_");

            log.info("🎭 MOCKING SERVICE: Generating event [{}] - ID: {}",
                    mockEvent.getEventType(), mockEvent.getId());

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, routingKey, eventJson);

            log.debug("📤 Sent to queue - Routing Key: {} | Payload size: {} bytes",
                    routingKey, eventJson.length());

        } catch (JsonProcessingException e) {
            log.error("❌ Error serializing mock event", e);
        }
    }

    private WebhookEvent createRandomMockEvent() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String eventType = EVENT_TYPES[random.nextInt(EVENT_TYPES.length)];

        // Create more realistic mock data based on event type
        Map<String, Object> eventData = createEventTypeSpecificData(eventType, random);

        return WebhookEvent.builder()
                .id(UUID.randomUUID().toString())
                .eventType(eventType)
                .source(SOURCE[random.nextInt(SOURCE.length)])
                .timestamp(LocalDateTime.now())
                .userId("user-" + random.nextInt(10000))
                .amount(generateRealisticAmount(eventType, random))
                .currency(CURRENCY[random.nextInt(CURRENCY.length)])
                .status(STATUS[random.nextInt(STATUS.length)])
                .data(eventData)
                .build();
    }

    private Map<String, Object> createEventTypeSpecificData(String eventType, ThreadLocalRandom random) {
        Map<String, Object> baseData = Map.of(
                "transactionId", "txn-" + UUID.randomUUID().toString().substring(0, 8),
                "sessionId", "sess-" + random.nextInt(100000),
                "environment", random.nextBoolean() ? "production" : "staging"
        );

        // Add event-specific data
        if (eventType.startsWith("payment")) {
            return Map.of(
                    "paymentMethod", random.nextBoolean() ? "card" : "bank_transfer",
                    "merchantId", "merchant-" + random.nextInt(500),
                    "gatewayResponse", "APPROVED",
                    "processingTime", random.nextInt(5000) + "ms"
            );
        } else if (eventType.startsWith("user")) {
            return Map.of(
                    "userType", random.nextBoolean() ? "premium" : "standard",
                    "registrationSource", random.nextBoolean() ? "organic" : "referral",
                    "country", random.nextBoolean() ? "NG" : "US"
            );
        } else if (eventType.startsWith("order")) {
            return Map.of(
                    "orderId", "ORD-" + random.nextInt(100000),
                    "itemCount", random.nextInt(10) + 1,
                    "shippingMethod", random.nextBoolean() ? "express" : "standard",
                    "warehouseLocation", "WH-" + random.nextInt(10)
            );
        }

        return baseData;
    }

    private Double generateRealisticAmount(String eventType, ThreadLocalRandom random) {
        if (eventType.contains("payment") || eventType.contains("order")) {
            double[] commonAmounts = {5, 10, 15, 20, 25, 30, 50, 75, 100, 150, 200, 250, 300, 500, 750, 1000};
            return commonAmounts[random.nextInt(commonAmounts.length)];
        } else if (eventType.contains("refund")) {
            double[] refundAmounts = {10, 20, 25, 50, 75, 100, 150, 200, 300, 500};
            return refundAmounts[random.nextInt(refundAmounts.length)];
        } else if (eventType.contains("subscription")) {
            double[] subscriptionAmounts = {5, 10, 15, 20, 25, 30, 50, 100};
            return subscriptionAmounts[random.nextInt(subscriptionAmounts.length)];
        }
        return random.nextDouble(1.0, 100.0);
    }


}
