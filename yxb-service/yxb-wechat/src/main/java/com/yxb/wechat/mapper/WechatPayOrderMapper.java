package com.yxb.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxb.wechat.domain.entity.WechatPayOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付订单Mapper
 */
@Mapper
public interface WechatPayOrderMapper extends BaseMapper<WechatPayOrder> {
}
