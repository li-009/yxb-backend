package com.yxb.video.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.result.Result;
import com.yxb.video.biz.AdminVideoBiz;
import com.yxb.video.domain.vo.VideoCreateRequest;
import com.yxb.video.domain.vo.VideoPageRequest;
import com.yxb.video.domain.vo.VideoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台-视频管理接口
 */
@Tag(name = "管理后台-视频管理")
@RestController
@RequestMapping("/admin/video")
@RequiredArgsConstructor
public class AdminVideoController {

    private final AdminVideoBiz adminVideoBiz;

    @Operation(summary = "分页查询视频")
    @GetMapping("/page")
    public Result<IPage<VideoDTO>> page(VideoPageRequest request) {
        return Result.success(adminVideoBiz.pageVideos(request));
    }

    @Operation(summary = "获取视频详情")
    @GetMapping("/{id}")
    public Result<VideoDTO> getById(@PathVariable Long id) {
        return Result.success(adminVideoBiz.getById(id));
    }

    @Operation(summary = "创建视频")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody VideoCreateRequest request) {
        return Result.success(adminVideoBiz.createVideo(request));
    }

    @Operation(summary = "更新视频")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody VideoUpdateRequest request) {
        adminVideoBiz.updateVideo(request);
        return Result.success();
    }

    @Operation(summary = "删除视频")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminVideoBiz.deleteVideo(id);
        return Result.success();
    }

    @Operation(summary = "更新视频状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        adminVideoBiz.updateStatus(id, status);
        return Result.success();
    }
}
