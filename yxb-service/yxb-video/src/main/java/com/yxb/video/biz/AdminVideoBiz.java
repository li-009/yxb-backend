package com.yxb.video.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.video.convert.VideoConvert;
import com.yxb.video.domain.entity.Video;
import com.yxb.video.domain.vo.VideoCreateRequest;
import com.yxb.video.domain.vo.VideoPageRequest;
import com.yxb.video.domain.vo.VideoUpdateRequest;
import com.yxb.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 管理后台-视频管理业务
 */
@Component
@RequiredArgsConstructor
public class AdminVideoBiz {

    private final VideoService videoService;
    private final VideoConvert videoConvert;

    /**
     * 分页查询视频
     */
    public IPage<VideoDTO> pageVideos(VideoPageRequest request) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getTitle())) {
            wrapper.like(Video::getTitle, request.getTitle());
        }
        if (StringUtils.hasText(request.getLanguage())) {
            wrapper.eq(Video::getLanguage, request.getLanguage());
        }
        if (request.getLevel() != null) {
            wrapper.eq(Video::getLevel, request.getLevel());
        }
        if (request.getCategoryId() != null) {
            wrapper.eq(Video::getCategoryId, request.getCategoryId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Video::getStatus, request.getStatus());
        }
        if (request.getSource() != null) {
            wrapper.eq(Video::getSource, request.getSource());
        }
        wrapper.eq(Video::getDeleted, 0);
        wrapper.orderByDesc(Video::getId);
        Page<Video> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<Video> videoPage = videoService.page(page, wrapper);
        return videoPage.convert(videoConvert::toDTO);
    }

    /**
     * 获取视频详情
     */
    public VideoDTO getById(Long id) {
        Video video = videoService.getById(id);
        if (video == null || video.getDeleted() == 1) {
            throw new BizException("视频不存在");
        }
        return videoConvert.toDTO(video);
    }

    /**
     * 创建视频
     */
    public Long createVideo(VideoCreateRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setCoverUrl(request.getCoverUrl());
        video.setVideoUrl(request.getVideoUrl());
        video.setDuration(request.getDuration());
        video.setFormat(request.getFormat());
        video.setFileSize(request.getFileSize());
        video.setSource(request.getSource() != null ? request.getSource() : 1);
        video.setSourcePlatform(request.getSourcePlatform());
        video.setOriginalUrl(request.getOriginalUrl());
        video.setLanguage(request.getLanguage());
        video.setLevel(request.getLevel() != null ? request.getLevel() : 1);
        video.setCategoryId(request.getCategoryId());
        video.setTags(request.getTags());
        video.setPlayCount(0);
        video.setCollectCount(0);
        video.setHasSubtitle(false);
        video.setStatus(1);
        videoService.save(video);
        return video.getId();
    }

    /**
     * 更新视频
     */
    public void updateVideo(VideoUpdateRequest request) {
        Video video = videoService.getById(request.getId());
        if (video == null || video.getDeleted() == 1) {
            throw new BizException("视频不存在");
        }
        if (StringUtils.hasText(request.getTitle())) {
            video.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            video.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getCoverUrl())) {
            video.setCoverUrl(request.getCoverUrl());
        }
        if (StringUtils.hasText(request.getVideoUrl())) {
            video.setVideoUrl(request.getVideoUrl());
        }
        if (request.getDuration() != null) {
            video.setDuration(request.getDuration());
        }
        if (StringUtils.hasText(request.getFormat())) {
            video.setFormat(request.getFormat());
        }
        if (request.getFileSize() != null) {
            video.setFileSize(request.getFileSize());
        }
        if (request.getSource() != null) {
            video.setSource(request.getSource());
        }
        if (StringUtils.hasText(request.getSourcePlatform())) {
            video.setSourcePlatform(request.getSourcePlatform());
        }
        if (StringUtils.hasText(request.getOriginalUrl())) {
            video.setOriginalUrl(request.getOriginalUrl());
        }
        if (StringUtils.hasText(request.getLanguage())) {
            video.setLanguage(request.getLanguage());
        }
        if (request.getLevel() != null) {
            video.setLevel(request.getLevel());
        }
        if (request.getCategoryId() != null) {
            video.setCategoryId(request.getCategoryId());
        }
        if (request.getTags() != null) {
            video.setTags(request.getTags());
        }
        videoService.updateById(video);
    }

    /**
     * 删除视频（逻辑删除）
     */
    public void deleteVideo(Long id) {
        Video video = videoService.getById(id);
        if (video == null) {
            throw new BizException("视频不存在");
        }
        video.setDeleted(1);
        videoService.updateById(video);
    }

    /**
     * 更新视频状态（上架/下架）
     */
    public void updateStatus(Long id, Integer status) {
        Video video = videoService.getById(id);
        if (video == null || video.getDeleted() == 1) {
            throw new BizException("视频不存在");
        }
        video.setStatus(status);
        videoService.updateById(video);
    }
}
