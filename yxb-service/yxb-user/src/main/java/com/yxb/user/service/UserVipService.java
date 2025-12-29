package com.yxb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.user.domain.entity.UserVip;

import java.util.List;

/**
 * 用户VIP Service接口
 */
public interface UserVipService extends IService<UserVip> {

    /**
     * 获取用户当前有效的VIP
     */
    UserVip getActiveVip(Long userId);

    /**
     * 获取用户所有VIP记录
     */
    List<UserVip> getVipHistory(Long userId);

    /**
     * 检查用户是否是VIP
     */
    boolean isVip(Long userId);

    /**
     * 获取用户VIP等级
     */
    int getVipLevel(Long userId);
}
