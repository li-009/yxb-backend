package com.yxb.video.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "视频上传请求")
public class VideoUploadRequest {

    @NotBlank(message = "标题不能为空")
    @Schema(description = "视频标题")
    private String title;

    @Schema(description = "视频描述")
    private String description;

    @NotBlank(message = "语言不能为空")
    @Schema(description = "语言代码，如en/ja/ko")
    private String language;

    @Schema(description = "难度等级 1-5")
    private Integer level;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签，逗号分隔")
    private String tags;
}
