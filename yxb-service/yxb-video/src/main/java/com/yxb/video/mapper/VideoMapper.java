package com.yxb.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.video.domain.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    
    IPage<Video> selectPageByCondition(Page<Video> page, @Param("language") String language,
                                        @Param("level") Integer level, @Param("categoryId") Long categoryId);
    
    int incrementPlayCount(@Param("id") Long id);
    
    int incrementCollectCount(@Param("id") Long id);
    
    int decrementCollectCount(@Param("id") Long id);
    
    IPage<Video> search(Page<Video> page, @Param("keyword") String keyword,
                        @Param("language") String language, @Param("level") Integer level);
}
