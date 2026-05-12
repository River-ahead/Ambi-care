package com.mindcare.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WhisperClient {

    public String transcribe(byte[] audioBytes) {
        log.info("WhisperClient.transcribe 被调用（当前为 Mock 占位实现）");
        return "（Mock 转写结果）这里是从语音识别到的文本内容。";
    }
}

