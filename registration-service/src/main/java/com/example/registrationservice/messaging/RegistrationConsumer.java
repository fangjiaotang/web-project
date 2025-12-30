package com.example.registrationservice.messaging;

import com.example.registrationservice.model.RegistrationRequest;
import com.example.registrationservice.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConsumer {
    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper;

    public RegistrationConsumer(RegistrationService registrationService, ObjectMapper objectMapper) {
        this.registrationService = registrationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "registration-requests", groupId = "registration-group")
    public void consume(String message) {
        try {
            RegistrationRequest request = objectMapper.readValue(message, RegistrationRequest.class);
            registrationService.processRegistration(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}