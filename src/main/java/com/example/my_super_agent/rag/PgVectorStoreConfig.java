package com.example.my_super_agent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

//@Configuration
public class PgVectorStoreConfig {
    @Autowired
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Autowired
    private MyKeyWordEnricher myKeyWordEnricher;

//
//    @Bean
//    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
//
//        List<Document> docsFromPdf = loveAppDocumentLoader.getDocsFromPdf();
//        PgVectorStore pgVectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
//                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
//                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
//                .indexType(HNSW)                     // Optional: defaults to HNSW
//                .initializeSchema(true)              // Optional: defaults to false
//                .schemaName("public")                // Optional: defaults to "public"
//                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
//                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
//                .build();
//        myKeyWordEnricher.enrich(docsFromPdf);
//        pgVectorStore.add(docsFromPdf);
//        return pgVectorStore;
//    }


}
