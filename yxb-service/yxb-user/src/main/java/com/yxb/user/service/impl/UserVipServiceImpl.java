package com.yxb.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.user.domain.entity.UserVip;
import com.yxb.user.mapper.UserVipMapper;
import com.yxb.user.service.UserVipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户VIP Service实现
 */
@Service
@RequiredArgsConstructor
public class UserVipServiceImpl extends ServiceImpl<UserVipMapper, UserVip> implements UserVipService {

    @Override
    public UserVip getActiveVip(Long userId) {
        return baseMapper.selectActiveByUserId(userId);
    }

    @Override
    public List<UserVip> getVipHistory(Long userId) {
        return baseMapper.selectListByUserId(userId);
    }

    @Override
    public boolean isVip(Long userId) {
        return getActiveVip(userId) != null;
    }

    @Override
    public int getVipLevel(Long userId) {
        UserVip vip = getActiveVip(userId);
        return vip != null ? vip.getVipLevel() : 0;
    }
}
