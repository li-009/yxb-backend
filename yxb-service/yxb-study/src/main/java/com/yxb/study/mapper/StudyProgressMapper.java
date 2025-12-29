package com.yxb.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.study.domain.entity.StudyProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudyProgressMapper extends BaseMapper<StudyProgress> {
    StudyProgress selectByUserAndVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);
    IPage<StudyProgress> selectPageByUser(Page<StudyProgress> page, @Param("userId") Long userId);
}
