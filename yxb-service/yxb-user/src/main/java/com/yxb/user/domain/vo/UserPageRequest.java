package com.yxb.user.domain.vo;

import lombok.Data;

/**
 * 用户分页查询请求
 */
@Data
public class UserPageRequest {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private String phone;

    private String nickname;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * VIP等级 0-普通 1-VIP 2-SVIP
     */
    private Integer vipLevel;
}
