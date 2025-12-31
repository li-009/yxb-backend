package com.yxb.study.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.study.domain.entity.UserCollect;
import com.yxb.study.mapper.UserCollectMapper;
import org.springframework.stereotype.Service;

@Service
public class UserCollectService extends ServiceImpl<UserCollectMapper, UserCollect> {

    public UserCollect getByUserAndVideo(Long userId, Long videoId) {
        LambdaQueryWrapper<UserCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCollect::getUserId, userId)
               .eq(UserCollect::getVideoId, videoId)
               .eq(UserCollect::getDeleted, 0);
        return getOne(wrapper);
    }

    public IPage<UserCollect> pageByUser(Long userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<UserCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCollect::getUserId, userId)
               .eq(UserCollect::getDeleted, 0)
               .orderByDesc(UserCollect::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    public int countByUser(Long userId) {
        LambdaQueryWrapper<UserCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCollect::getUserId, userId)
               .eq(UserCollect::getDeleted, 0);
        return (int) count(wrapper);
    }

    public boolean isCollected(Long userId, Long videoId) {
        return getByUserAndVideo(userId, videoId) != null;
    }
}
