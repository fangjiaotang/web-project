package com.example.registrationservice.service.impl;

import com.example.registrationservice.feign.ActivityServiceClient;
import com.example.registrationservice.feign.UserServiceClient;
import com.example.registrationservice.model.Registration;
import com.example.registrationservice.model.RegistrationRequest;
import com.example.registrationservice.repository.RegistrationRepository;
import com.example.registrationservice.service.RegistrationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ActivityServiceClient activityServiceClient;
    private final UserServiceClient userServiceClient;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, ActivityServiceClient activityServiceClient, UserServiceClient userServiceClient) {
        this.registrationRepository = registrationRepository;
        this.activityServiceClient = activityServiceClient;
        this.userServiceClient = userServiceClient;
    }

    @Override
    @Transactional
    public Registration register(RegistrationRequest request) {
        // 验证活动是否可以报名
        boolean isRegistrationOpen = activityServiceClient.isRegistrationOpen(request.getActivityId());
        if (!isRegistrationOpen) {
            throw new RuntimeException("Registration is not open for this activity");
        }

        // 验证用户是否存在
        // boolean userExists = userServiceClient.userExists(request.getUserId());
        // if (!userExists) {
        //     throw new RuntimeException("User not found");
        // }

        // 检查用户是否已经报名
        if (isUserRegistered(request.getUserId(), request.getActivityId())) {
            throw new RuntimeException("User already registered for this activity");
        }

        try {
            // 增加活动参与者数量
            boolean participantsIncremented = activityServiceClient.incrementParticipants(request.getActivityId());
            if (!participantsIncremented) {
                throw new RuntimeException("Failed to update activity participants");
            }

            // 创建报名记录
            Registration registration = new Registration();
            registration.setUserId(request.getUserId());
            registration.setActivityId(request.getActivityId());
            return registrationRepository.save(registration);
        } catch (DataIntegrityViolationException e) {
            // 如果创建报名记录失败，回滚活动参与者数量
            activityServiceClient.decrementParticipants(request.getActivityId());
            throw new RuntimeException("Failed to register, please try again later");
        } catch (Exception e) {
            // 其他异常也回滚活动参与者数量
            activityServiceClient.decrementParticipants(request.getActivityId());
            throw e;
        }
    }

    @Override
    public Optional<Registration> getRegistrationById(Long id) {
        return registrationRepository.findById(id);
    }

    @Override
    public Optional<Registration> getRegistrationByUserIdAndActivityId(Long userId, Long activityId) {
        return registrationRepository.findByUserIdAndActivityId(userId, activityId);
    }

    @Override
    public List<Registration> getRegistrationsByUserId(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    @Override
    public List<Registration> getRegistrationsByActivityId(Long activityId) {
        return registrationRepository.findByActivityId(activityId);
    }

    @Override
    public Long getRegistrationCountByActivityId(Long activityId) {
        return registrationRepository.countByActivityIdAndStatus(activityId, "SUCCESS");
    }

    @Override
    @Transactional
    public boolean cancelRegistration(Long id) {
        Optional<Registration> registration = registrationRepository.findById(id);
        if (registration.isPresent()) {
            Registration r = registration.get();
            r.setStatus("CANCELLED");
            registrationRepository.save(r);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserRegistered(Long userId, Long activityId) {
        return registrationRepository.existsByUserIdAndActivityId(userId, activityId);
    }

    @Override
    public void processRegistration(RegistrationRequest request) {
        // 异步处理报名请求
        try {
            register(request);
        } catch (Exception e) {
            // 记录错误日志，可选择重试或通知用户
            e.printStackTrace();
        }
    }
}