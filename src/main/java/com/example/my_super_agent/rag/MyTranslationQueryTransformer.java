package com.example.my_super_agent.rag;

import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.util.StringUtils;

public class MyTranslationQueryTransformer implements QueryTransformer {
    @Override
    public Query transform(Query query) {
        // 调用翻译服务

        String translatedQuery = "翻译后的查询";
        if (!StringUtils.hasText(translatedQuery)){
            return query;
        }
        return new Query(translatedQuery);
    }


}
