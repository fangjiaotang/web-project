package com.example.activityservice.service;

import com.example.activityservice.model.Activity;

import java.util.List;
import java.util.Optional;

public interface ActivityService {
    Activity createActivity(Activity activity);
    Optional<Activity> getActivityById(Long id);
    List<Activity> getAvailableActivities();
    Activity updateActivity(Activity activity);
    void deleteActivity(Long id);
}