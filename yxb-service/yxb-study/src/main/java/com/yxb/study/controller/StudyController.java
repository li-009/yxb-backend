package com.yxb.study.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.study.dto.StudyProgressDTO;
import com.yxb.api.study.dto.WordBookDTO;
import com.yxb.common.core.result.Result;
import com.yxb.study.biz.StudyBiz;
import com.yxb.study.domain.entity.WordBook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "学习管理")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyBiz studyBiz;

    @Operation(summary = "更新学习进度")
    @PostMapping("/progress/update")
    public Result<Void> updateProgress(@RequestParam Long videoId,
                                        @RequestParam Integer position,
                                        @RequestParam Integer watchedDuration) {
        studyBiz.updateProgress(videoId, position, watchedDuration);
        return Result.success();
    }

    @Operation(summary = "分页查询学习进度")
    @GetMapping("/progress/page")
    public Result<IPage<StudyProgressDTO>> pageProgress(@RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(studyBiz.pageProgress(pageNum, pageSize));
    }

    @Operation(summary = "添加生词")
    @PostMapping("/word/add")
    public Result<Void> addWord(@RequestBody WordBook wordBook) {
        studyBiz.addWord(wordBook);
        return Result.success();
    }

    @Operation(summary = "分页查询单词本")
    @GetMapping("/word/page")
    public Result<IPage<WordBookDTO>> pageWords(@RequestParam(required = false) Integer masteryStatus,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(studyBiz.pageWords(masteryStatus, pageNum, pageSize));
    }

    @Operation(summary = "获取待复习单词")
    @GetMapping("/word/review")
    public Result<List<WordBookDTO>> getReviewWords(@RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(studyBiz.getReviewWords(limit));
    }

    @Operation(summary = "更新单词掌握状态")
    @PostMapping("/word/{id}/mastery")
    public Result<Void> updateWordMastery(@PathVariable Long id, @RequestParam Integer masteryStatus) {
        studyBiz.updateWordMastery(id, masteryStatus);
        return Result.success();
    }
}
