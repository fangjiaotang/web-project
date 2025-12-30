package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setCollegeId(1L);

        // 模拟Repository行为
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // 执行测试
        User result = userService.getUserById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser() {
        // 准备测试数据
        User user = new User();
        user.setUsername("newuser");
        user.setCollegeId(1L);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setCollegeId(1L);

        // 模拟Repository行为
        when(userRepository.save(user)).thenReturn(savedUser);

        // 执行测试
        User result = userService.createUser(user);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        // 准备测试数据
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setCollegeId(1L);

        User updatedUser = new User();
        updatedUser.setUsername("newuser");
        updatedUser.setCollegeId(1L);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setCollegeId(1L);

        // 模拟Repository行为
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(savedUser);

        // 执行测试
        User result = userService.updateUser(1L, updatedUser);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        // 执行测试
        userService.deleteUser(1L);

        // 验证结果
        verify(userRepository, times(1)).deleteById(1L);
    }
}
