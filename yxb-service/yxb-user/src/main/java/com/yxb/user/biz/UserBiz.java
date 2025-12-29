package com.yxb.user.biz;

import cn.hutool.core.bean.BeanUtil;
import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.common.core.util.UserContextHolder;
import com.yxb.user.convert.UserConvert;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.vo.UserUpdateRequest;
import com.yxb.user.service.UserService;
import com.yxb.user.service.UserVipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户业务层
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserBiz {

    private final UserService userService;
    private final UserVipService userVipService;

    /**
     * 获取当前用户信息
     */
    public UserDTO getCurrentUser() {
        Long userId = UserContextHolder.requireUserId();
        User user = userService.getById(userId);
        AssertUtil.notNull(user, ResultCode.USER_NOT_EXIST);
        return UserConvert.INSTANCE.toDTO(user);
    }

    /**
     * 根据ID获取用户信息
     */
    public UserDTO getUserById(Long userId) {
        User user = userService.getById(userId);
        AssertUtil.notNull(user, ResultCode.USER_NOT_EXIST);
        return UserConvert.INSTANCE.toDTO(user);
    }

    /**
     * 更新用户信息
     */
    public UserDTO updateUser(UserUpdateRequest request) {
        Long userId = UserContextHolder.requireUserId();
        User user = userService.getById(userId);
        AssertUtil.notNull(user, ResultCode.USER_NOT_EXIST);

        // 更新字段
        BeanUtil.copyProperties(request, user, "id");
        userService.updateById(user);

        return UserConvert.INSTANCE.toDTO(user);
    }

    /**
     * 增加用户积分
     */
    public void addPoints(Long userId, Integer points) {
        AssertUtil.isTrue(points > 0, "积分必须大于0");
        userService.addPoints(userId, points);
        log.info("用户{}增加积分: {}", userId, points);
    }

    /**
     * 增加学习时长
     */
    public void addStudyTime(Long userId, Integer minutes) {
        AssertUtil.isTrue(minutes > 0, "学习时长必须大于0");
        userService.addStudyTime(userId, minutes);
        log.info("用户{}增加学习时长: {}分钟", userId, minutes);
    }

    /**
     * 检查VIP状态
     */
    public boolean checkVipStatus(Long userId) {
        return userVipService.isVip(userId);
    }

    /**
     * 获取VIP等级
     */
    public int getVipLevel(Long userId) {
        return userVipService.getVipLevel(userId);
    }
}
