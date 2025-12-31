package com.yxb.video.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.video.domain.entity.Video;

public interface VideoService extends IService<Video> {
    IPage<Video> pageByCondition(int pageNum, int pageSize, String language, Integer level, Long categoryId);
    IPage<Video> search(int pageNum, int pageSize, String keyword, String language, Integer level);
    boolean incrementPlayCount(Long id);
    boolean incrementCollectCount(Long id);
    boolean decrementCollectCount(Long id);
}
