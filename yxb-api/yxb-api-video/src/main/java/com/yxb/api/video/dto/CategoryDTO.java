package com.yxb.api.video.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频分类DTO
 */
@Data
public class CategoryDTO {

    private Long id;

    private String name;

    private String icon;

    private Long parentId;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    /**
     * 子分类
     */
    private List<CategoryDTO> children;
}
