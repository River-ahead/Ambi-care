package com.mindcare.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MediaPipeClient {

    public String analyzeEmotion(byte[] imageBytes) {
        log.info("MediaPipeClient.analyzeEmotion 被调用（当前为 Mock 占位实现）");
        return "NEUTRAL";
    }
}

