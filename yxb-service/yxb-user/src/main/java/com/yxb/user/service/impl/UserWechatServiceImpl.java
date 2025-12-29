package com.yxb.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.user.domain.entity.UserWechat;
import com.yxb.user.mapper.UserWechatMapper;
import com.yxb.user.service.UserWechatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户微信绑定Service实现
 */
@Service
@RequiredArgsConstructor
public class UserWechatServiceImpl extends ServiceImpl<UserWechatMapper, UserWechat> implements UserWechatService {

    @Override
    public UserWechat getByUnionId(String unionId) {
        return baseMapper.selectByUnionId(unionId);
    }

    @Override
    public UserWechat getByOpenIdApp(String openId) {
        return baseMapper.selectByOpenIdApp(openId);
    }

    @Override
    public UserWechat getByOpenIdMini(String openId) {
        return baseMapper.selectByOpenIdMini(openId);
    }

    @Override
    public UserWechat getByOpenIdMp(String openId) {
        return baseMapper.selectByOpenIdMp(openId);
    }

    @Override
    public UserWechat getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
