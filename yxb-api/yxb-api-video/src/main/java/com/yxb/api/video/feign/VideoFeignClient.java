package com.yxb.api.video.feign;

import com.yxb.api.video.dto.SubtitleDTO;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 视频服务Feign客户端
 */
@FeignClient(name = "yxb-video", contextId = "videoFeignClient", path = "/inner/video")
public interface VideoFeignClient {

    /**
     * 根据ID获取视频信息
     */
    @GetMapping("/{id}")
    Result<VideoDTO> getVideoById(@PathVariable("id") Long id);

    /**
     * 获取视频字幕列表
     */
    @GetMapping("/{id}/subtitles")
    Result<List<SubtitleDTO>> getVideoSubtitles(@PathVariable("id") Long id);

    /**
     * 获取指定语言的字幕
     */
    @GetMapping("/{id}/subtitle")
    Result<SubtitleDTO> getVideoSubtitle(@PathVariable("id") Long id, 
                                          @RequestParam("language") String language);

    /**
     * 增加播放次数
     */
    @PostMapping("/{id}/play-count/incr")
    Result<Void> incrementPlayCount(@PathVariable("id") Long id);

    /**
     * 增加收藏次数
     */
    @PostMapping("/{id}/collect-count/incr")
    Result<Void> incrementCollectCount(@PathVariable("id") Long id);

    /**
     * 减少收藏次数
     */
    @PostMapping("/{id}/collect-count/decr")
    Result<Void> decrementCollectCount(@PathVariable("id") Long id);
}
