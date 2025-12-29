package com.yxb.api.user.dto;

import lombok.Data;

/**
 * 统计概览DTO
 */
@Data
public class StatsOverviewDTO {

    /**
     * 总用户数
     */
    private Long totalUsers;

    /**
     * 今日新增用户
     */
    private Long todayNewUsers;

    /**
     * VIP用户数
     */
    private Long vipUsers;

    /**
     * 总视频数
     */
    private Long totalVideos;

    /**
     * 总播放量
     */
    private Long totalPlayCount;

    /**
     * 今日播放量
     */
    private Long todayPlayCount;

    /**
     * 活跃用户数（7日内有登录）
     */
    private Long activeUsers;
}
