package com.yxb.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.video.domain.entity.VideoSubtitle;
import java.util.List;

public interface VideoSubtitleService extends IService<VideoSubtitle> {
    List<VideoSubtitle> getByVideoId(Long videoId);
    VideoSubtitle getByVideoIdAndLanguage(Long videoId, String language);
}
