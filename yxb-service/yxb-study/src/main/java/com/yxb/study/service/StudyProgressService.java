package com.yxb.study.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.study.domain.entity.StudyProgress;

public interface StudyProgressService extends IService<StudyProgress> {
    StudyProgress getByUserAndVideo(Long userId, Long videoId);
    IPage<StudyProgress> pageByUser(Long userId, int pageNum, int pageSize);
}
