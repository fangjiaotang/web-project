package com.example.registrationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "activity-service")
public interface ActivityServiceClient {
    @GetMapping("/api/activities/{id}/registration/open")
    boolean isRegistrationOpen(@PathVariable("id") Long id);
    
    @PutMapping("/api/activities/{id}/participants/increment")
    boolean incrementParticipants(@PathVariable("id") Long id);
    
    @PutMapping("/api/activities/{id}/participants/decrement")
    boolean decrementParticipants(@PathVariable("id") Long id);
}