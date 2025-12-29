package com.yxb.video.controller.admin;

import com.yxb.api.video.dto.CategoryDTO;
import com.yxb.common.core.result.Result;
import com.yxb.video.biz.AdminCategoryBiz;
import com.yxb.video.domain.vo.CategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台-分类管理接口
 */
@Tag(name = "管理后台-分类管理")
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryBiz adminCategoryBiz;

    @Operation(summary = "获取分类树")
    @GetMapping("/tree")
    public Result<List<CategoryDTO>> tree() {
        return Result.success(adminCategoryBiz.listTree());
    }

    @Operation(summary = "获取所有分类")
    @GetMapping("/list")
    public Result<List<CategoryDTO>> list() {
        return Result.success(adminCategoryBiz.listAll());
    }

    @Operation(summary = "创建分类")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody CategoryRequest request) {
        return Result.success(adminCategoryBiz.create(request));
    }

    @Operation(summary = "更新分类")
    @PutMapping
    public Result<Void> update(@RequestBody CategoryRequest request) {
        adminCategoryBiz.update(request);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminCategoryBiz.delete(id);
        return Result.success();
    }
}
