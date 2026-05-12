package com.mindcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 向量库配置
 * 当前默认使用 InMemoryVectorStoreStub，便于本地运行。
 * 未来可在此处集成真实 Chroma / PGVector / Milvus 等向量数据库。
 */
@Configuration
public class ChromaConfig {

    @Bean
    public InMemoryVectorStoreStub inMemoryVectorStoreStub() {
        return new InMemoryVectorStoreStub();
    }
}

