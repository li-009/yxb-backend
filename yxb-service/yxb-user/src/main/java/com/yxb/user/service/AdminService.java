package com.yxb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.user.domain.entity.Admin;

public interface AdminService extends IService<Admin> {

    /**
     * 根据用户名查询管理员
     */
    Admin getByUsername(String username);
}
