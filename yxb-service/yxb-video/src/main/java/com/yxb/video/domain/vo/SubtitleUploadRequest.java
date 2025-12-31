package com.yxb.video.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "字幕上传请求")
public class SubtitleUploadRequest {

    @NotNull(message = "视频ID不能为空")
    @Schema(description = "视频ID")
    private Long videoId;

    @NotBlank(message = "语言不能为空")
    @Schema(description = "字幕语言代码，如en/zh/ja")
    private String language;

    @Schema(description = "字幕来源 1-用户上传 2-AI生成 3-第三方")
    private Integer source = 1;
}
