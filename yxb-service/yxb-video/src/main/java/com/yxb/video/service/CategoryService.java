package com.yxb.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.video.domain.entity.VideoCategory;

import java.util.List;

public interface CategoryService extends IService<VideoCategory> {

    /**
     * 根据父ID查询子分类
     */
    List<VideoCategory> listByParentId(Long parentId);
}
