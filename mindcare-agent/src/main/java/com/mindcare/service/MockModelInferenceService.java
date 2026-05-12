package com.mindcare.service;

import com.mindcare.dto.IntentDecision;
import com.mindcare.dto.RagDecision;
import lombok.extern.slf4j.Slf4j;

/**
 * Mock 模型推理服务：
 * - 关键词规则做意图识别和 RAG 规划
 * - 返回本地构造的自然语言回答
 */
@Slf4j
public class MockModelInferenceService implements ModelInferenceService {

    @Override
    public IntentDecision classifyIntent(String userContent) {
        String lower = userContent.toLowerCase();
        IntentDecision decision = new IntentDecision();
        if (lower.contains("自杀") || lower.contains("suicide") ||
                lower.contains("不想活") || lower.contains("伤害自己")) {
            decision.setIntent("RISK");
            decision.setReason("检测到自杀/自残等高风险表达");
        } else if (lower.contains("失眠") || lower.contains("焦虑")
                || lower.contains("抑郁") || lower.contains("压力")) {
            decision.setIntent("CONSULT");
            decision.setReason("检测到焦虑/失眠/抑郁等心理困扰表达");
        } else if (lower.contains("你好") || lower.contains("hello") ||
                lower.contains("hi") || lower.contains("早上好")) {
            decision.setIntent("CHAT");
            decision.setReason("检测到日常问候表达");
        } else {
            decision.setIntent("CONSULT");
            decision.setReason("未匹配到明显高危关键词，默认为心理咨询场景");
        }
        return decision;
    }

    @Override
    public RagDecision planRag(String userContent) {
        RagDecision decision = new RagDecision();
        String lower = userContent.toLowerCase();
        if (lower.contains("失眠") || lower.contains("焦虑") || lower.contains("抑郁")) {
            decision.setAction("RETRIEVE");
            decision.setQuery(userContent);
            decision.setNeedMultiStep(false);
        } else {
            decision.setAction("ANSWER");
            decision.setQuery(userContent);
            decision.setNeedMultiStep(false);
        }
        return decision;
    }

    @Override
    public String generateAnswer(String promptWithContext) {
        String preview = promptWithContext;
        if (preview.length() > 180) {
            preview = preview.substring(0, 180) + "...";
        }
        String answer = "（模拟回答）我已经认真阅读了你的描述，并基于内置的心理健康知识进行初步分析：\n"
                + preview + "\n\n"
                + "虽然我是一个程序，无法替代专业医生或咨询师，但我鼓励你在需要时主动寻求身边的老师、家人或专业心理援助渠道的支持。";
        log.info("Mock 模型返回回答: {}", answer);
        return answer;
    }
}

