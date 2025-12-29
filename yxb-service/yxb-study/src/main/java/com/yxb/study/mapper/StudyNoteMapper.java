package com.yxb.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.study.domain.entity.StudyNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudyNoteMapper extends BaseMapper<StudyNote> {
    List<StudyNote> selectByVideoId(@Param("userId") Long userId, @Param("videoId") Long videoId);
    IPage<StudyNote> selectPageByUser(Page<StudyNote> page, @Param("userId") Long userId);
}
