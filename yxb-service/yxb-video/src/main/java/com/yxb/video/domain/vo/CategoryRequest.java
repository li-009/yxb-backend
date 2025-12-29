package com.yxb.video.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分类请求
 */
@Data
public class CategoryRequest {

    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private String icon;

    private Long parentId;

    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;
}
