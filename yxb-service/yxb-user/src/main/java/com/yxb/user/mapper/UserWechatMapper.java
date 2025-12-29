package com.yxb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxb.user.domain.entity.UserWechat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户微信绑定Mapper
 */
@Mapper
public interface UserWechatMapper extends BaseMapper<UserWechat> {

    /**
     * 根据UnionID查询
     */
    UserWechat selectByUnionId(@Param("unionId") String unionId);

    /**
     * 根据APP OpenID查询
     */
    UserWechat selectByOpenIdApp(@Param("openId") String openId);

    /**
     * 根据小程序OpenID查询
     */
    UserWechat selectByOpenIdMini(@Param("openId") String openId);

    /**
     * 根据公众号OpenID查询
     */
    UserWechat selectByOpenIdMp(@Param("openId") String openId);

    /**
     * 根据用户ID查询
     */
    UserWechat selectByUserId(@Param("userId") Long userId);
}
