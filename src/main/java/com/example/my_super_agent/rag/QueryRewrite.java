package com.example.my_super_agent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写
 */
@Component
public class QueryRewrite {

    private final QueryTransformer queryTransformer;


    public QueryRewrite(ChatModel dashscopeChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(dashscopeChatModel);
        this.queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();

    }

    /**
     * 重写查询
     *
     * @param query
     * @return
     */
    public String rewrite(String query) {
        return queryTransformer.apply(new Query(query)).text();

    }
}
