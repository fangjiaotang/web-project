package com.example.registrationservice.service;

import com.example.registrationservice.model.Registration;
import com.example.registrationservice.model.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface RegistrationService {
    Registration register(RegistrationRequest request);
    Optional<Registration> getRegistrationById(Long id);
    Optional<Registration> getRegistrationByUserIdAndActivityId(Long userId, Long activityId);
    List<Registration> getRegistrationsByUserId(Long userId);
    List<Registration> getRegistrationsByActivityId(Long activityId);
    Long getRegistrationCountByActivityId(Long activityId);
    boolean cancelRegistration(Long id);
    boolean isUserRegistered(Long userId, Long activityId);
    void processRegistration(RegistrationRequest request);
}