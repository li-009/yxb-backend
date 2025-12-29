package com.yxb.user.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxb.api.user.dto.StatsOverviewDTO;
import com.yxb.user.domain.entity.User;
import com.yxb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 管理后台-数据统计业务
 */
@Component
@RequiredArgsConstructor
public class AdminStatsBiz {

    private final UserService userService;

    /**
     * 获取统计概览
     */
    public StatsOverviewDTO getOverview() {
        StatsOverviewDTO dto = new StatsOverviewDTO();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        dto.setTotalUsers(userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)));
        dto.setTodayNewUsers(userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .ge(User::getCreateTime, todayStart)));
        dto.setVipUsers(userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .gt(User::getVipLevel, 0)));
        dto.setActiveUsers(userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .ge(User::getLastLoginTime, weekAgo)));
        dto.setTotalVideos(0L);
        dto.setTotalPlayCount(0L);
        dto.setTodayPlayCount(0L);
        return dto;
    }
}
