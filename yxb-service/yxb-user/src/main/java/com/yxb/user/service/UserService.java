package com.yxb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.user.domain.entity.User;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询
     */
    User getByUsername(String username);

    /**
     * 根据手机号查询
     */
    User getByPhone(String phone);

    /**
     * 增加积分
     */
    boolean addPoints(Long userId, Integer points);

    /**
     * 增加学习时长
     */
    boolean addStudyTime(Long userId, Integer minutes);

    /**
     * 增加学习天数
     */
    boolean addStudyDays(Long userId, Integer days);

    /**
     * 更新最后登录信息
     */
    boolean updateLastLogin(Long userId, String ip);
}
