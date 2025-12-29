package com.yxb.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.video.dto.SubtitleDTO;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.result.Result;
import com.yxb.video.biz.VideoBiz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "视频管理")
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoBiz videoBiz;

    @Operation(summary = "获取视频详情")
    @GetMapping("/{id}")
    public Result<VideoDTO> getById(@PathVariable Long id) {
        return Result.success(videoBiz.getById(id));
    }

    @Operation(summary = "分页查询视频")
    @GetMapping("/page")
    public Result<IPage<VideoDTO>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String language,
                                         @RequestParam(required = false) Integer level,
                                         @RequestParam(required = false) Long categoryId) {
        return Result.success(videoBiz.pageVideos(pageNum, pageSize, language, level, categoryId));
    }

    @Operation(summary = "获取视频字幕列表")
    @GetMapping("/{id}/subtitles")
    public Result<List<SubtitleDTO>> getSubtitles(@PathVariable Long id) {
        return Result.success(videoBiz.getSubtitles(id));
    }

    @Operation(summary = "获取指定语言字幕")
    @GetMapping("/{id}/subtitle")
    public Result<SubtitleDTO> getSubtitle(@PathVariable Long id, @RequestParam String language) {
        return Result.success(videoBiz.getSubtitle(id, language));
    }

    @Operation(summary = "增加播放次数")
    @PostMapping("/{id}/play")
    public Result<Void> play(@PathVariable Long id) {
        videoBiz.incrementPlayCount(id);
        return Result.success();
    }
}
