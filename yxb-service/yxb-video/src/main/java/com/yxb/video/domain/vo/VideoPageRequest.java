package com.yxb.video.domain.vo;

import lombok.Data;

/**
 * 视频分页查询请求
 */
@Data
public class VideoPageRequest {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private String title;

    private String language;

    private Integer level;

    private Long categoryId;

    /**
     * 状态 0-下架 1-上架
     */
    private Integer status;

    /**
     * 来源 1-上传 2-YouTube 3-其他
     */
    private Integer source;
}
