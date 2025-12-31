package com.yxb.video.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.yxb.common.core.exception.BizException;
import com.yxb.video.config.OssConfig;
import com.yxb.video.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OssServiceImpl implements OssService {

    private final OSS ossClient;
    private final OssConfig ossConfig;

    @Autowired
    public OssServiceImpl(@Autowired(required = false) OSS ossClient, OssConfig ossConfig) {
        this.ossClient = ossClient;
        this.ossConfig = ossConfig;
    }

    private void checkOssEnabled() {
        if (ossClient == null) {
            throw new BizException("OSS服务未配置，请先配置OSS参数");
        }
    }

    private static final List<String> VIDEO_EXT = Arrays.asList("mp4", "mov", "avi", "mkv", "flv", "webm");
    private static final List<String> SUBTITLE_EXT = Arrays.asList("srt", "vtt", "ass", "ssa");
    private static final List<String> IMAGE_EXT = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    @Override
    public String uploadVideo(MultipartFile file) {
        validateFile(file, VIDEO_EXT, 500 * 1024 * 1024L, "视频");
        return upload(file, "video");
    }

    @Override
    public String uploadSubtitle(MultipartFile file) {
        validateFile(file, SUBTITLE_EXT, 5 * 1024 * 1024L, "字幕");
        return upload(file, "subtitle");
    }

    @Override
    public String uploadCover(MultipartFile file) {
        validateFile(file, IMAGE_EXT, 10 * 1024 * 1024L, "封面");
        return upload(file, "cover");
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        try {
            String key = fileUrl.substring(fileUrl.indexOf(ossConfig.getBucket()) + ossConfig.getBucket().length() + 1);
            ossClient.deleteObject(ossConfig.getBucket(), key);
            log.info("删除文件: {}", key);
        } catch (Exception e) {
            log.error("删除文件失败: {}", fileUrl, e);
        }
    }

    @Override
    public String getFileUrl(String objectKey) {
        return "https://" + ossConfig.getBucket() + "." + ossConfig.getEndpoint() + "/" + objectKey;
    }

    private String upload(MultipartFile file, String folder) {
        checkOssEnabled();
        String ext = getExtension(file.getOriginalFilename());
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String key = folder + "/" + date + "/" + IdUtil.fastSimpleUUID() + "." + ext;

        try (InputStream is = file.getInputStream()) {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.getSize());
            meta.setContentType(file.getContentType());
            ossClient.putObject(ossConfig.getBucket(), key, is, meta);
            String url = getFileUrl(key);
            log.info("上传成功: {}", url);
            return url;
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new BizException("文件上传失败");
        }
    }

    private void validateFile(MultipartFile file, List<String> allowedExt, long maxSize, String type) {
        if (file == null || file.isEmpty()) {
            throw new BizException(type + "文件不能为空");
        }
        if (file.getSize() > maxSize) {
            throw new BizException(type + "文件大小超过限制");
        }
        String ext = getExtension(file.getOriginalFilename());
        if (!allowedExt.contains(ext.toLowerCase())) {
            throw new BizException(type + "格式不支持，支持: " + String.join(",", allowedExt));
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf(".");
        return idx > 0 ? filename.substring(idx + 1) : "";
    }
}
