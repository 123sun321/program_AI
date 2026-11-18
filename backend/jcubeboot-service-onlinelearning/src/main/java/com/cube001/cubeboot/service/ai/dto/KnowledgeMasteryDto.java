package com.cube001.cubeboot.service.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class KnowledgeMasteryDto {
    private String knowledge;
    private Integer masteryScore;
    private Integer exerciseCount;
    private String level;
    private String weakness;
    private List<String> suggestions;
}
