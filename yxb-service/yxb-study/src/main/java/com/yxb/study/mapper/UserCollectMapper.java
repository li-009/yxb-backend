package com.yxb.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxb.study.domain.entity.UserCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCollectMapper extends BaseMapper<UserCollect> {
    UserCollect selectByUserAndVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);
    IPage<UserCollect> selectPageByUser(Page<UserCollect> page, @Param("userId") Long userId);
    int deleteByUserAndVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);
}
