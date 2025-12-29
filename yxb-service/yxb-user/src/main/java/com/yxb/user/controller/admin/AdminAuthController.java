package com.yxb.user.controller.admin;

import com.yxb.api.user.dto.AdminDTO;
import com.yxb.api.user.dto.AdminLoginDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.AdminAuthBiz;
import com.yxb.user.domain.vo.AdminLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台-认证接口
 */
@Tag(name = "管理后台-认证管理")
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthBiz adminAuthBiz;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<AdminLoginDTO> login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return Result.success(adminAuthBiz.login(request, ip));
    }

    @Operation(summary = "获取当前管理员信息")
    @GetMapping("/info")
    public Result<AdminDTO> getInfo() {
        return Result.success(adminAuthBiz.getCurrentAdmin());
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        adminAuthBiz.logout();
        return Result.success();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
