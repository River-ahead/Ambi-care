package com.mindcare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskAlert {

    private Long userId;

    private String userName;

    private double riskScore;

    private String reason;
}

