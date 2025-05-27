package com.example.my_super_agent.rag;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义rag检索增强顾问工厂
 */
public class MyCustomAdvisorFactory {

    public static Advisor createRagCustomAdvisor(VectorStore vectorStore,String status) {

        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.73) // 搜索相似度阈值
                .topK(5) // 搜索数量,取前5个最相似的
                .filterExpression(new FilterExpressionBuilder() // 过滤条件
                        .eq("status",status)
                        .build())
                .build();


        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .queryAugmenter(MyContextualQueryAugmenterFactory.create())
                .build();
    }


}
