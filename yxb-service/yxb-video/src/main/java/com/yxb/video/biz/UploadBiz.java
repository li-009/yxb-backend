package com.yxb.video.biz;

import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.video.domain.entity.Video;
import com.yxb.video.domain.entity.VideoSubtitle;
import com.yxb.video.domain.vo.SubtitleUploadRequest;
import com.yxb.video.domain.vo.UploadResult;
import com.yxb.video.domain.vo.VideoUploadRequest;
import com.yxb.video.service.OssService;
import com.yxb.video.service.VideoService;
import com.yxb.video.service.VideoSubtitleService;
import com.yxb.video.subtitle.SubtitleParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadBiz {

    private final OssService ossService;
    private final VideoService videoService;
    private final VideoSubtitleService subtitleService;
    private final SubtitleParser subtitleParser;

    @Transactional(rollbackFor = Exception.class)
    public Long uploadVideo(MultipartFile file, VideoUploadRequest request) {
        String videoUrl = ossService.uploadVideo(file);
        
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setVideoUrl(videoUrl);
        video.setLanguage(request.getLanguage());
        video.setLevel(request.getLevel() != null ? request.getLevel() : 1);
        video.setCategoryId(request.getCategoryId());
        video.setTags(request.getTags());
        video.setFileSize(file.getSize());
        video.setFormat(getExtension(file.getOriginalFilename()));
        video.setSource(1);
        video.setPlayCount(0);
        video.setCollectCount(0);
        video.setHasSubtitle(false);
        video.setStatus(1);
        
        videoService.save(video);
        log.info("视频上传成功, videoId={}, url={}", video.getId(), videoUrl);
        return video.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long uploadSubtitle(MultipartFile file, SubtitleUploadRequest request) {
        Video video = videoService.getById(request.getVideoId());
        AssertUtil.notNull(video, "视频不存在");

        String fileUrl = ossService.uploadSubtitle(file);
        String content = readFileContent(file);
        String format = getExtension(file.getOriginalFilename());

        subtitleParser.parse(content, format);

        VideoSubtitle subtitle = new VideoSubtitle();
        subtitle.setVideoId(request.getVideoId());
        subtitle.setLanguage(request.getLanguage());
        subtitle.setFormat(format);
        subtitle.setSource(request.getSource());
        subtitle.setFileUrl(fileUrl);
        subtitle.setContent(content);
        subtitle.setStatus(1);

        subtitleService.save(subtitle);

        video.setHasSubtitle(true);
        videoService.updateById(video);

        log.info("字幕上传成功, subtitleId={}, videoId={}", subtitle.getId(), request.getVideoId());
        return subtitle.getId();
    }

    public UploadResult uploadCover(MultipartFile file) {
        String url = ossService.uploadCover(file);
        return UploadResult.builder()
                .url(url)
                .filename(file.getOriginalFilename())
                .size(file.getSize())
                .build();
    }

    public String uploadFile(MultipartFile file, String type) {
        return switch (type.toLowerCase()) {
            case "video" -> ossService.uploadVideo(file);
            case "subtitle" -> ossService.uploadSubtitle(file);
            case "cover" -> ossService.uploadCover(file);
            default -> throw new BizException("不支持的文件类型: " + type);
        };
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf(".");
        return idx > 0 ? filename.substring(idx + 1).toLowerCase() : "";
    }

    private String readFileContent(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BizException("读取文件内容失败");
        }
    }
}
