package com.yxb.video.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "上传结果")
public class UploadResult {

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "文件名")
    private String filename;

    @Schema(description = "文件大小(字节)")
    private Long size;
}
