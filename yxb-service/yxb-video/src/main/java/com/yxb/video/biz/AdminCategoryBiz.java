package com.yxb.video.biz;

import com.yxb.api.video.dto.CategoryDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.video.domain.entity.VideoCategory;
import com.yxb.video.domain.vo.CategoryRequest;
import com.yxb.video.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理后台-分类管理业务
 */
@Component
@RequiredArgsConstructor
public class AdminCategoryBiz {

    private final CategoryService categoryService;

    /**
     * 获取分类列表（树形结构）
     */
    public List<CategoryDTO> listTree() {
        List<VideoCategory> rootList = categoryService.listByParentId(0L);
        return rootList.stream()
                .map(this::buildTree)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有分类（平铺）
     */
    public List<CategoryDTO> listAll() {
        List<VideoCategory> all = categoryService.list();
        return all.stream()
                .filter(c -> c.getDeleted() == 0)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 创建分类
     */
    public Long create(CategoryRequest request) {
        VideoCategory category = new VideoCategory();
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
        category.setSort(request.getSort() != null ? request.getSort() : 0);
        category.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        categoryService.save(category);
        return category.getId();
    }

    /**
     * 更新分类
     */
    public void update(CategoryRequest request) {
        if (request.getId() == null) {
            throw new BizException("分类ID不能为空");
        }
        VideoCategory category = categoryService.getById(request.getId());
        if (category == null || category.getDeleted() == 1) {
            throw new BizException("分类不存在");
        }
        if (StringUtils.hasText(request.getName())) {
            category.setName(request.getName());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getParentId() != null) {
            category.setParentId(request.getParentId());
        }
        if (request.getSort() != null) {
            category.setSort(request.getSort());
        }
        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }
        categoryService.updateById(category);
    }

    /**
     * 删除分类
     */
    public void delete(Long id) {
        VideoCategory category = categoryService.getById(id);
        if (category == null) {
            throw new BizException("分类不存在");
        }
        List<VideoCategory> children = categoryService.listByParentId(id);
        if (!children.isEmpty()) {
            throw new BizException("存在子分类，无法删除");
        }
        category.setDeleted(1);
        categoryService.updateById(category);
    }

    private CategoryDTO buildTree(VideoCategory category) {
        CategoryDTO dto = toDTO(category);
        List<VideoCategory> children = categoryService.listByParentId(category.getId());
        if (!children.isEmpty()) {
            dto.setChildren(children.stream()
                    .map(this::buildTree)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private CategoryDTO toDTO(VideoCategory category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        dto.setParentId(category.getParentId());
        dto.setSort(category.getSort());
        dto.setStatus(category.getStatus());
        dto.setCreateTime(category.getCreateTime());
        return dto;
    }
}
