package com.yxb.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.video.domain.entity.VideoCategory;
import com.yxb.video.mapper.CategoryMapper;
import com.yxb.video.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, VideoCategory> implements CategoryService {

    @Override
    public List<VideoCategory> listByParentId(Long parentId) {
        return list(new LambdaQueryWrapper<VideoCategory>()
                .eq(VideoCategory::getParentId, parentId != null ? parentId : 0L)
                .eq(VideoCategory::getDeleted, 0)
                .orderByAsc(VideoCategory::getSort));
    }
}
