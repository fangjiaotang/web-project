package com.example.registrationservice.service;

import com.example.registrationservice.feign.ActivityServiceClient;
import com.example.registrationservice.feign.UserServiceClient;
import com.example.registrationservice.model.Registration;
import com.example.registrationservice.model.RegistrationRequest;
import com.example.registrationservice.repository.RegistrationRepository;
import com.example.registrationservice.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ActivityServiceClient activityServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private Registration testRegistration;
    private RegistrationRequest testRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 准备测试数据
        testRegistration = new Registration();
        testRegistration.setId(1L);
        testRegistration.setUserId(1L);
        testRegistration.setActivityId(1L);
        testRegistration.setStatus("SUCCESS");

        testRequest = new RegistrationRequest();
        testRequest.setUserId(1L);
        testRequest.setActivityId(1L);
    }

    @Test
    void testRegisterSuccess() {
        // 模拟依赖行为
        when(activityServiceClient.isRegistrationOpen(1L)).thenReturn(true);
        when(registrationRepository.existsByUserIdAndActivityId(1L, 1L)).thenReturn(false);
        when(activityServiceClient.incrementParticipants(1L)).thenReturn(true);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // 执行测试
        Registration result = registrationService.register(testRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getActivityId());
        verify(activityServiceClient, times(1)).isRegistrationOpen(1L);
        verify(registrationRepository, times(1)).existsByUserIdAndActivityId(1L, 1L);
        verify(activityServiceClient, times(1)).incrementParticipants(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }



    @Test
    void testRegisterAlreadyRegistered() {
        // 模拟依赖行为
        when(activityServiceClient.isRegistrationOpen(1L)).thenReturn(true);
        when(registrationRepository.existsByUserIdAndActivityId(1L, 1L)).thenReturn(true);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(testRequest);
        });

        // 验证结果
        assertEquals("User already registered for this activity", exception.getMessage());
        verify(activityServiceClient, times(1)).isRegistrationOpen(1L);
        verify(registrationRepository, times(1)).existsByUserIdAndActivityId(1L, 1L);
        verify(activityServiceClient, never()).incrementParticipants(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testRegisterRollbackOnError() {
        // 模拟依赖行为
        when(activityServiceClient.isRegistrationOpen(1L)).thenReturn(true);
        when(registrationRepository.existsByUserIdAndActivityId(1L, 1L)).thenReturn(false);
        when(activityServiceClient.incrementParticipants(1L)).thenReturn(true);
        when(registrationRepository.save(any(Registration.class))).thenThrow(new DataIntegrityViolationException("Database error"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(testRequest);
        });

        // 验证结果
        assertEquals("Failed to register, please try again later", exception.getMessage());
        verify(activityServiceClient, times(1)).isRegistrationOpen(1L);
        verify(registrationRepository, times(1)).existsByUserIdAndActivityId(1L, 1L);
        verify(activityServiceClient, times(1)).incrementParticipants(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
        verify(activityServiceClient, times(1)).decrementParticipants(1L);
    }

    @Test
    void testGetRegistrationById() {
        // 模拟依赖行为
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));

        // 执行测试
        Optional<Registration> result = registrationService.getRegistrationById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(registrationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRegistrationByUserIdAndActivityId() {
        // 模拟依赖行为
        when(registrationRepository.findByUserIdAndActivityId(1L, 1L)).thenReturn(Optional.of(testRegistration));

        // 执行测试
        Optional<Registration> result = registrationService.getRegistrationByUserIdAndActivityId(1L, 1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(registrationRepository, times(1)).findByUserIdAndActivityId(1L, 1L);
    }

    @Test
    void testGetRegistrationsByUserId() {
        // 准备测试数据
        List<Registration> registrations = Arrays.asList(testRegistration);

        // 模拟依赖行为
        when(registrationRepository.findByUserId(1L)).thenReturn(registrations);

        // 执行测试
        List<Registration> result = registrationService.getRegistrationsByUserId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(registrationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetRegistrationsByActivityId() {
        // 准备测试数据
        List<Registration> registrations = Arrays.asList(testRegistration);

        // 模拟依赖行为
        when(registrationRepository.findByActivityId(1L)).thenReturn(registrations);

        // 执行测试
        List<Registration> result = registrationService.getRegistrationsByActivityId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(registrationRepository, times(1)).findByActivityId(1L);
    }

    @Test
    void testGetRegistrationCountByActivityId() {
        // 模拟依赖行为
        when(registrationRepository.countByActivityIdAndStatus(1L, "SUCCESS")).thenReturn(50L);

        // 执行测试
        Long result = registrationService.getRegistrationCountByActivityId(1L);

        // 验证结果
        assertEquals(50L, result);
        verify(registrationRepository, times(1)).countByActivityIdAndStatus(1L, "SUCCESS");
    }

    @Test
    void testCancelRegistration() {
        // 准备测试数据
        Registration cancelledRegistration = new Registration(testRegistration);
        cancelledRegistration.setStatus("CANCELLED");

        // 模拟依赖行为
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(cancelledRegistration);

        // 执行测试
        boolean result = registrationService.cancelRegistration(1L);

        // 验证结果
        assertTrue(result);
        verify(registrationRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testIsUserRegistered() {
        // 模拟依赖行为
        when(registrationRepository.existsByUserIdAndActivityId(1L, 1L)).thenReturn(true);

        // 执行测试
        boolean result = registrationService.isUserRegistered(1L, 1L);

        // 验证结果
        assertTrue(result);
        verify(registrationRepository, times(1)).existsByUserIdAndActivityId(1L, 1L);
    }

    @Test
    void testProcessRegistration() {
        // 模拟依赖行为
        when(activityServiceClient.isRegistrationOpen(1L)).thenReturn(true);
        when(registrationRepository.existsByUserIdAndActivityId(1L, 1L)).thenReturn(false);
        when(activityServiceClient.incrementParticipants(1L)).thenReturn(true);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // 执行测试（异步处理，无返回值）
        registrationService.processRegistration(testRequest);

        // 验证调用
        verify(activityServiceClient, times(1)).isRegistrationOpen(1L);
        verify(registrationRepository, times(1)).existsByUserIdAndActivityId(1L, 1L);
        verify(activityServiceClient, times(1)).incrementParticipants(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
}