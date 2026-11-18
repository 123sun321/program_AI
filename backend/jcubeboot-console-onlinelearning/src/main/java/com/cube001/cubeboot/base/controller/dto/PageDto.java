package com.cube001.cubeboot.base.controller.dto;

import lombok.Data;

@Data
public class PageDto {
    private  Integer current = 1;
    private  Integer size = 10;
    private String order = "creationTime";
}
