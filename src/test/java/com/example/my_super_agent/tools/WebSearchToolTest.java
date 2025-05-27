package com.example.my_super_agent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSearchToolTest {
    @Value("${searchApi.apiKey}")
    private String API_KEY;

    @Test
    void search() {
        WebSearchTool webSearchTool = new WebSearchTool(API_KEY);

        String result = webSearchTool.WebSearch("上海静安区5公里内合适的约会地点");
        System.out.println(result);


    }
}