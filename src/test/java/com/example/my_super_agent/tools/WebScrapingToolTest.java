package com.example.my_super_agent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void scrape() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String content = webScrapingTool.scrape("https://www.baidu.com");
        Assertions.assertNotNull(content);
    }
}