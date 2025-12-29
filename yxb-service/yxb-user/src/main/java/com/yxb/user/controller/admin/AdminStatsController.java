package com.yxb.user.controller.admin;

import com.yxb.api.user.dto.StatsOverviewDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.AdminStatsBiz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理后台-数据统计接口
 */
@Tag(name = "管理后台-数据统计")
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsBiz adminStatsBiz;

    @Operation(summary = "获取统计概览")
    @GetMapping("/overview")
    public Result<StatsOverviewDTO> overview() {
        return Result.success(adminStatsBiz.getOverview());
    }
}
