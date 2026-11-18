package com.cube001.cubeboot.service.ai.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PracticeRecordDto {
    private Long trainingId;
    private Long exerciseId;
    private String exerciseName;
    private String knowledge;
    private String html;
    private String css;
    private String js;
    private Date joinTime;
}
