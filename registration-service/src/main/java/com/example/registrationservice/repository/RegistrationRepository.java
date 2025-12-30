package com.example.registrationservice.repository;

import com.example.registrationservice.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByUserIdAndActivityId(Long userId, Long activityId);
    List<Registration> findByUserId(Long userId);
    List<Registration> findByActivityId(Long activityId);
    List<Registration> findByActivityIdAndStatus(Long activityId, String status);
    Long countByActivityIdAndStatus(Long activityId, String status);
    boolean existsByUserIdAndActivityId(Long userId, Long activityId);
}