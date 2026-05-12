package com.mindcare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmotionResult {

    private double score;

    private String label;

    private boolean highRisk;

    private String reason;
}

