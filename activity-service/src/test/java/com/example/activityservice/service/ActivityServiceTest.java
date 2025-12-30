package com.example.activityservice.service;

import com.example.activityservice.model.Activity;
import com.example.activityservice.repository.ActivityRepository;
import com.example.activityservice.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity testActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 准备测试数据
        testActivity = new Activity();
        testActivity.setId(1L);
        testActivity.setName("测试活动");
        testActivity.setStatus("ACTIVE");
        testActivity.setStartTime(LocalDateTime.now().plusDays(1));
        testActivity.setEndTime(LocalDateTime.now().plusDays(2));
        testActivity.setLocation("测试地点");
    }

    @Test
    void testCreateActivity() {
        // 模拟Repository行为
        when(activityRepository.save(any(Activity.class))).thenReturn(testActivity);

        // 执行测试
        Activity result = activityService.createActivity(testActivity);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试活动", result.getName());
        verify(activityRepository, times(1)).save(testActivity);
    }

    @Test
    void testGetActivityById() {
        // 模拟Repository行为
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));

        // 执行测试
        Optional<Activity> result = activityService.getActivityById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(activityRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAvailableActivities() {
        // 准备测试数据
        List<Activity> activities = Arrays.asList(testActivity);

        // 模拟Repository行为
        when(activityRepository.findByStatus("ACTIVE")).thenReturn(activities);

        // 执行测试
        List<Activity> result = activityService.getAvailableActivities();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findByStatus("ACTIVE");
    }

    @Test
    void testUpdateActivity() {
        // 准备测试数据
        testActivity.setName("更新后的活动");

        // 模拟Repository行为
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(activityRepository.save(any(Activity.class))).thenReturn(testActivity);

        // 执行测试
        Activity result = activityService.updateActivity(testActivity);

        // 验证结果
        assertNotNull(result);
        assertEquals("更新后的活动", result.getName());
        verify(activityRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).save(testActivity);
    }

    @Test
    void testDeleteActivity() {
        // 执行测试
        activityService.deleteActivity(1L);

        // 验证结果
        verify(activityRepository, times(1)).deleteById(1L);
    }
}