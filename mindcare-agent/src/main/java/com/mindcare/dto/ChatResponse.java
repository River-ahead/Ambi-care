package com.mindcare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponse {

    private String answer;

    private String intent;

    private String emotionLabel;

    private Double emotionScore;
}

