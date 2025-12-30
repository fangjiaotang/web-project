package com.example.registrationservice.model;

public class RegistrationRequest {
    private Long userId;
    private Long activityId;

    public RegistrationRequest() {
    }

    public RegistrationRequest(Long userId, Long activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}