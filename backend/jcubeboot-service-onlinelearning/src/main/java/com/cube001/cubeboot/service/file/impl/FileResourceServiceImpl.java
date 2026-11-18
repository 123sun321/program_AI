package com.cube001.cubeboot.service.file.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.file.entity.FileResource;
import com.cube001.cubeboot.service.file.FileResourceService;
import com.cube001.cubeboot.service.file.dto.FileResourceDto;
import com.cube001.cubeboot.service.file.mapper.FileResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author anonymous
 * @since 2019-03-14
 */
@Service
public class FileResourceServiceImpl extends ServiceImpl<FileResourceMapper, FileResource> implements FileResourceService {

}
