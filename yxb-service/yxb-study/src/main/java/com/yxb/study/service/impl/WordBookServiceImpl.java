package com.yxb.study.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.study.domain.entity.WordBook;
import com.yxb.study.mapper.WordBookMapper;
import com.yxb.study.service.WordBookService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WordBookServiceImpl extends ServiceImpl<WordBookMapper, WordBook> implements WordBookService {
    @Override
    public IPage<WordBook> pageByUser(Long userId, Integer masteryStatus, int pageNum, int pageSize) {
        return baseMapper.selectPageByUser(new Page<>(pageNum, pageSize), userId, masteryStatus);
    }

    @Override
    public List<WordBook> getNeedReview(Long userId, int limit) {
        return baseMapper.selectNeedReview(userId, limit);
    }

    @Override
    public WordBook getByUserAndWord(Long userId, String word) {
        return baseMapper.selectByUserAndWord(userId, word);
    }
}
