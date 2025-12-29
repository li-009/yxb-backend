package com.yxb.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.study.domain.entity.WordBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WordBookMapper extends BaseMapper<WordBook> {
    IPage<WordBook> selectPageByUser(Page<WordBook> page, @Param("userId") Long userId, @Param("masteryStatus") Integer masteryStatus);
    List<WordBook> selectNeedReview(@Param("userId") Long userId, @Param("limit") Integer limit);
    WordBook selectByUserAndWord(@Param("userId") Long userId, @Param("word") String word);
}
