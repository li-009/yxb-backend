package com.yxb.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxb.video.domain.entity.VideoCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<VideoCategory> {
}
