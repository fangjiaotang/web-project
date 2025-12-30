package com.example.activityservice.messaging;

import com.example.activityservice.model.Activity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActivityProducer {
    private final KafkaTemplate<String, Activity> kafkaTemplate;
    private static final String ACTIVITY_TOPIC = "activity-events";

    public ActivityProducer(KafkaTemplate<String, Activity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendActivityCreated(Activity activity) {
        kafkaTemplate.send(ACTIVITY_TOPIC, "CREATED", activity);
    }

    public void sendActivityUpdated(Activity activity) {
        kafkaTemplate.send(ACTIVITY_TOPIC, "UPDATED", activity);
    }

    public void sendActivityDeleted(Long activityId) {
        Activity activity = new Activity();
        activity.setId(activityId);
        kafkaTemplate.send(ACTIVITY_TOPIC, "DELETED", activity);
    }
}