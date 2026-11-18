package com.cube001.cubeboot.service.training;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.training.entity.Code;

public interface CodeService extends IService<Code> {
    /**
     * 创建代码
     * @author anonymous
     * @param codeDto
     * @return
     */
    Long insert(CodeDto codeDto);

    /**
     * 更新代码
     * @author anonymous
     * @param codeDto
     * @return
     */
    Long update(CodeDto codeDto);

    /**
     * 获取实验数据
     * @author anonymous
     * @param id
     * @return
     */
    CodeDto get(Long id);

    /**
     * 更新实验代码
     * @author anonymous
     * @param codeDto
     */
    void updateCodeDto(Long userId,CodeDto codeDto);

    /**
      * @description
      * @author anonymous
      * @date 2025/7/4 14:57
      * @param
      * @return
     */
    Code selectByAssignmentSubmitId(Long id);
}
