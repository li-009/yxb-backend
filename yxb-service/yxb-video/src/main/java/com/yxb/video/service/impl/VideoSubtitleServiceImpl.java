package com.yxb.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.video.domain.entity.VideoSubtitle;
import com.yxb.video.mapper.VideoSubtitleMapper;
import com.yxb.video.service.VideoSubtitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoSubtitleServiceImpl extends ServiceImpl<VideoSubtitleMapper, VideoSubtitle> implements VideoSubtitleService {

    @Override
    public List<VideoSubtitle> getByVideoId(Long videoId) {
        return baseMapper.selectByVideoId(videoId);
    }

    @Override
    public VideoSubtitle getByVideoIdAndLanguage(Long videoId, String language) {
        return baseMapper.selectByVideoIdAndLanguage(videoId, language);
    }
}
