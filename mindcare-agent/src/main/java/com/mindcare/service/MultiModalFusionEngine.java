package com.mindcare.service;

import com.mindcare.dto.EmotionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 多模态情绪融合引擎（预留），当前仅透传文本情绪结果。
 */
@Service
@Slf4j
public class MultiModalFusionEngine {

    public EmotionResult fuse(EmotionResult textEmotion) {
        log.debug("多模态融合占位，目前仅使用文本情绪: {}", textEmotion);
        return textEmotion;
    }
}

