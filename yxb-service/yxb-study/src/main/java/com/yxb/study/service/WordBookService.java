package com.yxb.study.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.study.domain.entity.WordBook;
import java.util.List;

public interface WordBookService extends IService<WordBook> {
    IPage<WordBook> pageByUser(Long userId, Integer masteryStatus, int pageNum, int pageSize);
    List<WordBook> getNeedReview(Long userId, int limit);
    WordBook getByUserAndWord(Long userId, String word);
}
