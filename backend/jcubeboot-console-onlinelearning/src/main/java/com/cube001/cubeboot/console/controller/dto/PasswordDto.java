package com.cube001.cubeboot.console.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordDto {
    @NotBlank
    private String oldPassword;
    @NotBlank
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message = "密码必须是8-20位的数字和字母的组合")
    private String newPassword;
}
