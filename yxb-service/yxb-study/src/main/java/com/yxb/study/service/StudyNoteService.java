package com.yxb.study.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.study.domain.entity.StudyNote;
import com.yxb.study.mapper.StudyNoteMapper;
import org.springframework.stereotype.Service;

@Service
public class StudyNoteService extends ServiceImpl<StudyNoteMapper, StudyNote> {

    public IPage<StudyNote> pageByUser(Long userId, Long videoId, int pageNum, int pageSize) {
        LambdaQueryWrapper<StudyNote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyNote::getUserId, userId)
               .eq(StudyNote::getDeleted, false);
        if (videoId != null) {
            wrapper.eq(StudyNote::getVideoId, videoId);
        }
        wrapper.orderByDesc(StudyNote::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    public int countByUser(Long userId) {
        LambdaQueryWrapper<StudyNote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyNote::getUserId, userId)
               .eq(StudyNote::getDeleted, false);
        return (int) count(wrapper);
    }
}
