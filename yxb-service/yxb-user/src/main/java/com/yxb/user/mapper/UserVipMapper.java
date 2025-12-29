package com.yxb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxb.user.domain.entity.UserVip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户VIP Mapper
 */
@Mapper
public interface UserVipMapper extends BaseMapper<UserVip> {

    /**
     * 查询用户有效的VIP记录
     */
    UserVip selectActiveByUserId(@Param("userId") Long userId);

    /**
     * 查询用户所有VIP记录
     */
    List<UserVip> selectListByUserId(@Param("userId") Long userId);
}
