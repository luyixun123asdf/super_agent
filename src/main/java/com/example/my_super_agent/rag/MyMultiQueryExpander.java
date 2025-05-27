package com.example.my_super_agent.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MyMultiQueryExpander {

    private final ChatClient.Builder chatClientBuilder;

    public MyMultiQueryExpander(ChatModel dashscopeChatModel){
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> expand(String query) {
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .includeOriginal(false)
                .build();
        List<Query> queries = queryExpander.expand(new Query("How to run a Spring Boot app?"));
        return queries;
    }
}
