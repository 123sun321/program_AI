package com.cube001.cubeboot.service.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class AnalyzeTaskDto {
    private String taskId;
    private String status;
    private Integer progress;
    private Integer totalItems;
    private Integer completedItems;
    private List<KnowledgeMasteryDto> result;
    private String errorMessage;
}
