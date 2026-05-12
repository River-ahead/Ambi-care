package com.mindcare.service;

import com.mindcare.dto.EmotionResult;
import com.mindcare.entity.EmotionRecord;
import com.mindcare.repository.EmotionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 情绪分析服务：当前基于关键词规则，输出结构化 EmotionResult，并入库。
 */
@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final EmotionRecordRepository emotionRecordRepository;

    public EmotionResult analyzeAndSave(Long userId, String content) {
        EmotionResult result = analyze(content);
        EmotionRecord record = new EmotionRecord();
        record.setUserId(userId);
        record.setLabel(result.getLabel());
        record.setScore(result.getScore());
        record.setHighRisk(result.isHighRisk());
        record.setReason(result.getReason());
        record.setCreatedAt(LocalDateTime.now());
        emotionRecordRepository.save(record);
        return result;
    }

    public EmotionResult analyze(String content) {
        String lower = content.toLowerCase();
        EmotionResult result = new EmotionResult();
        if (lower.contains("自杀") || lower.contains("不想活") || lower.contains("绝望") || lower.contains("suicide")) {
            result.setScore(4.0);
            result.setLabel("HIGH_RISK");
            result.setHighRisk(true);
            result.setReason("检测到自杀/绝望等高危表达");
        } else if (lower.contains("失眠") || lower.contains("焦虑") || lower.contains("紧张") || lower.contains("恐惧")) {
            result.setScore(3.0);
            result.setLabel("ANXIOUS");
            result.setHighRisk(false);
            result.setReason("检测到焦虑和紧张相关表达");
        } else if (lower.contains("难过") || lower.contains("伤心") || lower.contains("抑郁") || lower.contains("低落")) {
            result.setScore(2.5);
            result.setLabel("LOW");
            result.setHighRisk(false);
            result.setReason("检测到情绪低落相关表达");
        } else {
            result.setScore(1.0);
            result.setLabel("NORMAL");
            result.setHighRisk(false);
            result.setReason("未发现明显负面情绪关键词");
        }
        return result;
    }
}

