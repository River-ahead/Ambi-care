package com.mindcare.service;

import com.mindcare.dto.IntentDecision;
import com.mindcare.dto.RagDecision;

/**
 * 大模型推理统一接口
 * - 封装：意图识别、RAG 规划、生成回答
 * - Mock 模式与真实模型模式共享此接口
 */
public interface ModelInferenceService {

    IntentDecision classifyIntent(String userContent);

    RagDecision planRag(String userContent);

    String generateAnswer(String promptWithContext);
}

