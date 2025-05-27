package com.example.my_super_agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyKeyWordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

    public List<Document> enrich(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        return enricher.apply(documents);
    }


}
