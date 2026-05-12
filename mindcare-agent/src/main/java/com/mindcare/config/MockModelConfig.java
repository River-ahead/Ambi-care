package com.mindcare.config;

import com.mindcare.service.MockModelInferenceService;
import com.mindcare.service.ModelInferenceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mock 模型配置：
 * mindcare.mock-model=true 时启用 MockModelInferenceService。
 */
@Configuration
public class MockModelConfig {

    @Bean
    @ConditionalOnProperty(prefix = "mindcare", name = "mock-model", havingValue = "true", matchIfMissing = true)
    public ModelInferenceService mockModelInferenceService() {
        return new MockModelInferenceService();
    }
}

