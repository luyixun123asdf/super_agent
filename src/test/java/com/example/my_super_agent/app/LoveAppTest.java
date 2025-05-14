package com.example.my_super_agent.app;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void dochat() {
        String s = loveApp.dochat("你好,我叫不想写bug", "1");
        Assertions.assertNotNull(s);

    }
}