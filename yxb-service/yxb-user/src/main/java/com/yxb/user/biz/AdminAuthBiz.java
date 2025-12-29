package com.yxb.user.biz;

import cn.dev33.satoken.stp.StpUtil;
import com.yxb.api.user.dto.AdminDTO;
import com.yxb.api.user.dto.AdminLoginDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.user.domain.entity.Admin;
import com.yxb.user.domain.vo.AdminLoginRequest;
import com.yxb.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

/**
 * 管理员认证业务
 */
@Component
@RequiredArgsConstructor
public class AdminAuthBiz {

    private final AdminService adminService;

    /**
     * 管理员登录
     */
    public AdminLoginDTO login(AdminLoginRequest request, String ip) {
        Admin admin = adminService.getByUsername(request.getUsername());
        if (admin == null) {
            throw new BizException("用户名或密码错误");
        }
        if (admin.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }
        String encodedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!encodedPassword.equals(admin.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(ip);
        adminService.updateById(admin);
        StpUtil.login("admin:" + admin.getId());
        String token = StpUtil.getTokenValue();
        AdminLoginDTO dto = new AdminLoginDTO();
        dto.setToken(token);
        dto.setAdminInfo(toDTO(admin));
        return dto;
    }

    /**
     * 获取当前管理员信息
     */
    public AdminDTO getCurrentAdmin() {
        String loginId = (String) StpUtil.getLoginId();
        if (loginId == null || !loginId.startsWith("admin:")) {
            throw new BizException("未登录或非管理员账号");
        }
        Long adminId = Long.parseLong(loginId.replace("admin:", ""));
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            throw new BizException("管理员不存在");
        }
        return toDTO(admin);
    }

    /**
     * 退出登录
     */
    public void logout() {
        StpUtil.logout();
    }

    private AdminDTO toDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setRealName(admin.getRealName());
        dto.setPhone(admin.getPhone());
        dto.setEmail(admin.getEmail());
        dto.setAvatar(admin.getAvatar());
        dto.setRole(admin.getRole());
        dto.setStatus(admin.getStatus());
        dto.setLastLoginTime(admin.getLastLoginTime());
        return dto;
    }
}
