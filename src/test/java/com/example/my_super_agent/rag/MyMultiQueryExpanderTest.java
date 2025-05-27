package com.example.my_super_agent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyMultiQueryExpanderTest {
    @Resource
    MyMultiQueryExpander myMultiQueryExpander;



    @Test
    void expand() {
        List<Query> expand = myMultiQueryExpander.expand("How to run a Spring Boot app?");
        assertTrue(expand.size() > 0);
    }
}