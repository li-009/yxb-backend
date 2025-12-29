package com.yxb.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.user.domain.entity.User;
import com.yxb.user.mapper.UserMapper;
import com.yxb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户Service实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public User getByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public boolean addPoints(Long userId, Integer points) {
        return baseMapper.addPoints(userId, points) > 0;
    }

    @Override
    public boolean addStudyTime(Long userId, Integer minutes) {
        return baseMapper.addStudyTime(userId, minutes) > 0;
    }

    @Override
    public boolean addStudyDays(Long userId, Integer days) {
        return baseMapper.addStudyDays(userId, days) > 0;
    }

    @Override
    public boolean updateLastLogin(Long userId, String ip) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        return updateById(user);
    }
}
