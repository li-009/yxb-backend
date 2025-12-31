package com.yxb.video.controller;

import com.yxb.common.core.result.Result;
import com.yxb.video.biz.UploadBiz;
import com.yxb.video.domain.vo.SubtitleUploadRequest;
import com.yxb.video.domain.vo.UploadResult;
import com.yxb.video.domain.vo.VideoUploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadBiz uploadBiz;

    @Operation(summary = "上传视频")
    @PostMapping("/video")
    public Result<Long> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") VideoUploadRequest request) {
        return Result.success(uploadBiz.uploadVideo(file, request));
    }

    @Operation(summary = "上传字幕")
    @PostMapping("/subtitle")
    public Result<Long> uploadSubtitle(
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SubtitleUploadRequest request) {
        return Result.success(uploadBiz.uploadSubtitle(file, request));
    }

    @Operation(summary = "上传封面")
    @PostMapping("/cover")
    public Result<UploadResult> uploadCover(@RequestParam("file") MultipartFile file) {
        return Result.success(uploadBiz.uploadCover(file));
    }

    @Operation(summary = "简单上传文件(返回URL)")
    @PostMapping("/file")
    public Result<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        return Result.success(uploadBiz.uploadFile(file, type));
    }
}
