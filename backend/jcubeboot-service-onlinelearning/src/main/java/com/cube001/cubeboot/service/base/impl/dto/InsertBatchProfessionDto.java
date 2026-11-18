package com.cube001.cubeboot.service.base.impl.dto;

import lombok.Data;

@Data
public class InsertBatchProfessionDto {
    private Integer line;
    private String name;
    private Long departmentsId;
    private String code;
    private String msg;
}
