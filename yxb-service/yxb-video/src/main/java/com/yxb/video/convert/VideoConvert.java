package com.yxb.video.convert;

import com.yxb.api.video.dto.VideoDTO;
import com.yxb.video.domain.entity.Video;

public class VideoConvert {
    
    public static final VideoConvert INSTANCE = new VideoConvert();
    
    public VideoDTO toDTO(Video video) {
        if (video == null) return null;
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setCoverUrl(video.getCoverUrl());
        dto.setVideoUrl(video.getVideoUrl());
        dto.setDuration(video.getDuration());
        dto.setFormat(video.getFormat());
        dto.setFileSize(video.getFileSize());
        dto.setSource(video.getSource());
        dto.setSourcePlatform(video.getSourcePlatform());
        dto.setOriginalUrl(video.getOriginalUrl());
        dto.setLanguage(video.getLanguage());
        dto.setLevel(video.getLevel());
        dto.setCategoryId(video.getCategoryId());
        dto.setTags(video.getTags());
        dto.setPlayCount(video.getPlayCount());
        dto.setCollectCount(video.getCollectCount());
        dto.setHasSubtitle(video.getHasSubtitle());
        dto.setStatus(video.getStatus());
        dto.setUploadUserId(video.getUploadUserId());
        dto.setCreateTime(video.getCreateTime());
        return dto;
    }
}
