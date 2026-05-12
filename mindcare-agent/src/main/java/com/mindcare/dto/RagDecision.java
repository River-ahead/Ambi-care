package com.mindcare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RagDecision {

    private String action;

    private String query;

    private boolean needMultiStep;
}

