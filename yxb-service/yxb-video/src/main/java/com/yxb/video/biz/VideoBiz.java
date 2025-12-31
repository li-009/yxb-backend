package com.yxb.video.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.video.dto.SubtitleDTO;
import com.yxb.api.video.dto.VideoDTO;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.video.convert.VideoConvert;
import com.yxb.video.domain.entity.Video;
import com.yxb.video.domain.entity.VideoSubtitle;
import com.yxb.video.service.VideoService;
import com.yxb.video.service.VideoSubtitleService;
import com.yxb.video.subtitle.SubtitleParser;
import com.yxb.video.domain.vo.VideoImportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoBiz {

    private final VideoService videoService;
    private final VideoSubtitleService subtitleService;
    private final SubtitleParser subtitleParser;

    public VideoDTO getById(Long id) {
        Video video = videoService.getById(id);
        AssertUtil.notNull(video, ResultCode.VIDEO_NOT_FOUND);
        return VideoConvert.INSTANCE.toDTO(video);
    }

    public IPage<VideoDTO> pageVideos(int pageNum, int pageSize, String language, Integer level, Long categoryId) {
        IPage<Video> page = videoService.pageByCondition(pageNum, pageSize, language, level, categoryId);
        return page.convert(VideoConvert.INSTANCE::toDTO);
    }

    public List<SubtitleDTO> getSubtitles(Long videoId) {
        List<VideoSubtitle> subtitles = subtitleService.getByVideoId(videoId);
        return subtitles.stream().map(this::convertSubtitle).collect(Collectors.toList());
    }

    public SubtitleDTO getSubtitle(Long videoId, String language) {
        VideoSubtitle subtitle = subtitleService.getByVideoIdAndLanguage(videoId, language);
        AssertUtil.notNull(subtitle, "字幕不存在");
        return convertSubtitle(subtitle);
    }

    public void incrementPlayCount(Long id) {
        videoService.incrementPlayCount(id);
    }

    public void incrementCollectCount(Long id) {
        videoService.incrementCollectCount(id);
    }

    public void decrementCollectCount(Long id) {
        videoService.decrementCollectCount(id);
    }

    public IPage<VideoDTO> searchVideos(int pageNum, int pageSize, String keyword, String language, Integer level) {
        IPage<Video> page = videoService.search(pageNum, pageSize, keyword, language, level);
        return page.convert(VideoConvert.INSTANCE::toDTO);
    }

    public VideoDTO importByUrl(VideoImportRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setVideoUrl(request.getVideoUrl());
        video.setCoverUrl(request.getCoverUrl());
        video.setDuration(request.getDuration() != null ? request.getDuration() : 0);
        video.setLanguage(request.getLanguage() != null ? request.getLanguage() : "en");
        video.setLevel(request.getLevel() != null ? request.getLevel() : 1);
        video.setSource(2);
        video.setStatus(1);
        videoService.save(video);
        return VideoConvert.INSTANCE.toDTO(video);
    }

    public VideoDTO uploadVideo(MultipartFile file, String title, String description, String language, Integer level) {
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl("/uploads/" + file.getOriginalFilename());
        video.setLanguage(language);
        video.setLevel(level);
        video.setSource(1);
        video.setStatus(1);
        videoService.save(video);
        log.info("视频上传成功: {}", title);
        return VideoConvert.INSTANCE.toDTO(video);
    }

    private SubtitleDTO convertSubtitle(VideoSubtitle subtitle) {
        SubtitleDTO dto = new SubtitleDTO();
        dto.setId(subtitle.getId());
        dto.setVideoId(subtitle.getVideoId());
        dto.setLanguage(subtitle.getLanguage());
        dto.setFormat(subtitle.getFormat());
        dto.setSource(subtitle.getSource());
        dto.setFileUrl(subtitle.getFileUrl());
        if (subtitle.getContent() != null) {
            dto.setItems(subtitleParser.parse(subtitle.getContent(), subtitle.getFormat()));
        }
        return dto;
    }
}
