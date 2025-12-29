package com.yxb.user.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.AdminUserBiz;
import com.yxb.user.domain.vo.UserPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台-用户管理接口
 */
@Tag(name = "管理后台-用户管理")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserBiz adminUserBiz;

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public Result<IPage<UserDTO>> page(UserPageRequest request) {
        return Result.success(adminUserBiz.pageUsers(request));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<UserDTO> getById(@PathVariable Long id) {
        return Result.success(adminUserBiz.getById(id));
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        adminUserBiz.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "设置VIP")
    @PutMapping("/{id}/vip")
    public Result<Void> setVip(@PathVariable Long id, 
                               @RequestParam Integer vipLevel,
                               @RequestParam(required = false) Integer days) {
        adminUserBiz.setVip(id, vipLevel, days);
        return Result.success();
    }
}
