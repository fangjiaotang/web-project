package com.example.userservice.config;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public ApplicationRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 检查是否已经存在admin用户
            if (!userRepository.existsByUsername("admin")) {
                // 创建admin用户
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("123456"));
                adminUser.setName("管理员");
                adminUser.setEmail("admin@example.com");
                adminUser.setCollege("管理员学院");
                adminUser.setCollegeId(1L);
                adminUser.setPhone("13800138000");
                
                userRepository.save(adminUser);
                System.out.println("Admin user created: admin/123456");
            } else {
                System.out.println("Admin user already exists");
            }
        };
    }
}