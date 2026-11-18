package com.cube001.cubeboot.course.controller;

import com.cube001.cubeboot.core.base.controller.BaseController;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.training.entity.Code;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: PreviewController
 * 预览Controller
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/24 17:42
 */
@Controller
@RequestMapping("/preview")
public class PreviewController extends BaseController {

    @Autowired
    private CodeService codeService;
    /**
     * 预览
     * @param id 代码编号
     * @return
     */
    @GetMapping("/quite/{id}")
    public String quite(@PathVariable Long id, Model model){
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            CodeDto codeDto = new CodeDto();
            if (id != null) {
                codeDto = codeService.get(id);
            }
            model.addAttribute("html", codeDto.getHtml());
            model.addAttribute("css", codeDto.getCss());
            model.addAttribute("script", codeDto.getJs());
            return "/preview/index.html";
        }
    }
}
