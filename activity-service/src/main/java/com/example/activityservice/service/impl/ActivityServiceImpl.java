package com.example.activityservice.service.impl;

import com.example.activityservice.messaging.ActivityProducer;
import com.example.activityservice.model.Activity;
import com.example.activityservice.repository.ActivityRepository;
import com.example.activityservice.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityProducer activityProducer;

    @Override
    @CachePut(value = "activities", key = "#result.id")
    @CacheEvict(value = "availableActivities", allEntries = true)
    public Activity createActivity(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);
        activityProducer.sendActivityCreated(savedActivity);
        return savedActivity;
    }

    @Override
    @Cacheable(value = "activities", key = "#id")
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    @Cacheable(value = "availableActivities")
    public List<Activity> getAvailableActivities() {
        return activityRepository.findByStatus("ACTIVE");
    }

    @Override
    @CachePut(value = "activities", key = "#activity.id")
    @CacheEvict(value = "availableActivities", allEntries = true)
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivity = activityRepository.findById(activity.getId());
        if (existingActivity.isPresent()) {
            Activity updatedActivity = activityRepository.save(activity);
            activityProducer.sendActivityUpdated(updatedActivity);
            return updatedActivity;
        }
        return null;
    }

    @Override
    @CacheEvict(value = {"activities", "availableActivities"}, key = "#id", allEntries = true)
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
        activityProducer.sendActivityDeleted(id);
    }
}