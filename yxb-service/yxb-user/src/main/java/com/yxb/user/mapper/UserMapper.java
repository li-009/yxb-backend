package com.yxb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxb.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 增加积分
     */
    int addPoints(@Param("userId") Long userId, @Param("points") Integer points);

    /**
     * 增加学习时长
     */
    int addStudyTime(@Param("userId") Long userId, @Param("minutes") Integer minutes);

    /**
     * 增加学习天数
     */
    int addStudyDays(@Param("userId") Long userId, @Param("days") Integer days);
}
