package com.yxb.user.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.user.convert.UserConvert;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.vo.UserPageRequest;
import com.yxb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 管理后台-用户管理业务
 */
@Component
@RequiredArgsConstructor
public class AdminUserBiz {

    private final UserService userService;
    private final UserConvert userConvert;

    /**
     * 分页查询用户
     */
    public IPage<UserDTO> pageUsers(UserPageRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getPhone())) {
            wrapper.like(User::getPhone, request.getPhone());
        }
        if (StringUtils.hasText(request.getNickname())) {
            wrapper.like(User::getNickname, request.getNickname());
        }
        if (request.getStatus() != null) {
            wrapper.eq(User::getStatus, request.getStatus());
        }
        if (request.getVipLevel() != null) {
            wrapper.eq(User::getVipLevel, request.getVipLevel());
        }
        wrapper.eq(User::getDeleted, 0);
        wrapper.orderByDesc(User::getId);
        Page<User> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<User> userPage = userService.page(page, wrapper);
        return userPage.convert(userConvert::toDTO);
    }

    /**
     * 获取用户详情
     */
    public UserDTO getById(Long id) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BizException("用户不存在");
        }
        return userConvert.toDTO(user);
    }

    /**
     * 更新用户状态
     */
    public void updateStatus(Long id, Integer status) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BizException("用户不存在");
        }
        user.setStatus(status);
        userService.updateById(user);
    }

    /**
     * 设置VIP
     */
    public void setVip(Long id, Integer vipLevel, Integer days) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BizException("用户不存在");
        }
        user.setVipLevel(vipLevel);
        if (days != null && days > 0) {
            LocalDateTime expireTime = LocalDateTime.now().plusDays(days);
            if (user.getVipExpireTime() != null && user.getVipExpireTime().isAfter(LocalDateTime.now())) {
                expireTime = user.getVipExpireTime().plusDays(days);
            }
            user.setVipExpireTime(expireTime);
        }
        userService.updateById(user);
    }
}
