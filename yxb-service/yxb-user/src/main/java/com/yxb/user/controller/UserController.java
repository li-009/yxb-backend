package com.yxb.user.controller;

import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.UserBiz;
import com.yxb.user.domain.vo.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserBiz userBiz;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUser() {
        return Result.success(userBiz.getCurrentUser());
    }

    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        return Result.success(userBiz.getUserById(id));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public Result<UserDTO> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        return Result.success(userBiz.updateUser(request));
    }

    @Operation(summary = "检查VIP状态")
    @GetMapping("/vip/check")
    public Result<Boolean> checkVipStatus() {
        Long userId = com.yxb.common.core.util.UserContextHolder.requireUserId();
        return Result.success(userBiz.checkVipStatus(userId));
    }

    @Operation(summary = "获取VIP等级")
    @GetMapping("/vip/level")
    public Result<Integer> getVipLevel() {
        Long userId = com.yxb.common.core.util.UserContextHolder.requireUserId();
        return Result.success(userBiz.getVipLevel(userId));
    }
}
