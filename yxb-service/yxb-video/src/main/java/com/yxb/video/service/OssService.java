package com.yxb.video.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    String uploadVideo(MultipartFile file);

    String uploadSubtitle(MultipartFile file);

    String uploadCover(MultipartFile file);

    void deleteFile(String fileUrl);

    String getFileUrl(String objectKey);
}
