package com.yxb.video.controller.inner;

import com.yxb.api.video.dto.SubtitleDTO;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.result.Result;
import com.yxb.video.biz.VideoBiz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "视频内部接口")
@RestController
@RequestMapping("/inner/video")
@RequiredArgsConstructor
public class VideoInnerController {

    private final VideoBiz videoBiz;

    @GetMapping("/{id}")
    public Result<VideoDTO> getVideoById(@PathVariable Long id) {
        return Result.success(videoBiz.getById(id));
    }

    @GetMapping("/{id}/subtitles")
    public Result<List<SubtitleDTO>> getVideoSubtitles(@PathVariable Long id) {
        return Result.success(videoBiz.getSubtitles(id));
    }

    @GetMapping("/{id}/subtitle")
    public Result<SubtitleDTO> getVideoSubtitle(@PathVariable Long id, @RequestParam String language) {
        return Result.success(videoBiz.getSubtitle(id, language));
    }

    @PostMapping("/{id}/play-count/incr")
    public Result<Void> incrementPlayCount(@PathVariable Long id) {
        videoBiz.incrementPlayCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/collect-count/incr")
    public Result<Void> incrementCollectCount(@PathVariable Long id) {
        videoBiz.incrementCollectCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/collect-count/decr")
    public Result<Void> decrementCollectCount(@PathVariable Long id) {
        videoBiz.decrementCollectCount(id);
        return Result.success();
    }
}
