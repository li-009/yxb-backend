package com.yxb.video.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.video.domain.entity.Video;
import com.yxb.video.mapper.VideoMapper;
import com.yxb.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public IPage<Video> pageByCondition(int pageNum, int pageSize, String language, Integer level, Long categoryId) {
        return baseMapper.selectPageByCondition(new Page<>(pageNum, pageSize), language, level, categoryId);
    }

    @Override
    public boolean incrementPlayCount(Long id) {
        return baseMapper.incrementPlayCount(id) > 0;
    }

    @Override
    public boolean incrementCollectCount(Long id) {
        return baseMapper.incrementCollectCount(id) > 0;
    }

    @Override
    public boolean decrementCollectCount(Long id) {
        return baseMapper.decrementCollectCount(id) > 0;
    }
}
