package com.yxb.user.controller;

import com.yxb.api.user.dto.LoginDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.AuthBiz;
import com.yxb.user.domain.vo.LoginRequest;
import com.yxb.user.domain.vo.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthBiz authBiz;

    @Operation(summary = "统一登录")
    @PostMapping("/login")
    public Result<LoginDTO> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return Result.success(authBiz.login(request, ip));
    }

    @Operation(summary = "手机号注册")
    @PostMapping("/register")
    public Result<LoginDTO> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return Result.success(authBiz.register(request, ip));
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<LoginDTO> refreshToken() {
        return Result.success(authBiz.refreshToken());
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authBiz.logout();
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
