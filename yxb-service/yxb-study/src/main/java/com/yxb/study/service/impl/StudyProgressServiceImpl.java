package com.yxb.study.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.study.domain.entity.StudyProgress;
import com.yxb.study.mapper.StudyProgressMapper;
import com.yxb.study.service.StudyProgressService;
import org.springframework.stereotype.Service;

@Service
public class StudyProgressServiceImpl extends ServiceImpl<StudyProgressMapper, StudyProgress> implements StudyProgressService {
    @Override
    public StudyProgress getByUserAndVideo(Long userId, Long videoId) {
        return baseMapper.selectByUserAndVideo(userId, videoId);
    }

    @Override
    public IPage<StudyProgress> pageByUser(Long userId, int pageNum, int pageSize) {
        return baseMapper.selectPageByUser(new Page<>(pageNum, pageSize), userId);
    }
}
