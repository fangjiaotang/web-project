package com.example.activityservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class ActivityServiceApplicationTests {

    @Test
    void contextLoads() {
        // 只测试上下文是否能正常加载
    }

}