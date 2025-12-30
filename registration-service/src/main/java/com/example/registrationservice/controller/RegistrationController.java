package com.example.registrationservice.controller;

import com.example.registrationservice.messaging.RegistrationProducer;
import com.example.registrationservice.model.Registration;
import com.example.registrationservice.model.RegistrationRequest;
import com.example.registrationservice.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final RegistrationProducer registrationProducer;

    public RegistrationController(RegistrationService registrationService, RegistrationProducer registrationProducer) {
        this.registrationService = registrationService;
        this.registrationProducer = registrationProducer;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            // 使用消息队列异步处理报名请求
            registrationProducer.sendRegistrationRequest(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Registration request received, processing...");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to submit registration request");
        }
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Registration>> getRegistrationsByActivityId(@PathVariable Long activityId) {
        List<Registration> registrations = registrationService.getRegistrationsByActivityId(activityId);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Registration>> getRegistrationsByUserId(@PathVariable Long userId) {
        List<Registration> registrations = registrationService.getRegistrationsByUserId(userId);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/user/{userId}/activity/{activityId}")
    public ResponseEntity<Registration> getRegistrationByUserAndActivity(
            @PathVariable Long userId, @PathVariable Long activityId) {
        Optional<Registration> registration = registrationService.getRegistrationByUserIdAndActivityId(userId, activityId);
        return registration.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long id) {
        boolean cancelled = registrationService.cancelRegistration(id);
        if (cancelled) {
            return ResponseEntity.ok("Registration cancelled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registration not found");
        }
    }

    @GetMapping("/activity/{activityId}/count")
    public ResponseEntity<Long> getRegistrationCount(@PathVariable Long activityId) {
        Long count = registrationService.getRegistrationCountByActivityId(activityId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/user/{userId}/activity/{activityId}/exists")
    public ResponseEntity<Boolean> checkUserRegistration(
            @PathVariable Long userId, @PathVariable Long activityId) {
        boolean exists = registrationService.isUserRegistered(userId, activityId);
        return ResponseEntity.ok(exists);
    }
}