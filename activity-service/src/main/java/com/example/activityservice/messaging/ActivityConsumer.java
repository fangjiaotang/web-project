package com.example.activityservice.messaging;

import com.example.activityservice.model.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ActivityConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ActivityConsumer.class);
    private static final String ACTIVITY_TOPIC = "activity-events";

    @KafkaListener(topics = ACTIVITY_TOPIC, groupId = "activity-group")
    public void consumeActivityEvent(Activity activity, String key) {
        switch (key) {
            case "CREATED" -> logger.info("Received activity created event: {}", activity);
            case "UPDATED" -> logger.info("Received activity updated event: {}", activity);
            case "DELETED" -> logger.info("Received activity deleted event: ID = {}", activity.getId());
            default -> logger.warn("Received unknown activity event: Key = {}, Activity = {}", key, activity);
        }
    }
}