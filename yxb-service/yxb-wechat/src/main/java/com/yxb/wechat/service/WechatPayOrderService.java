package com.yxb.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.wechat.domain.entity.WechatPayOrder;

/**
 * 微信支付订单服务接口
 */
public interface WechatPayOrderService extends IService<WechatPayOrder> {

    /**
     * 处理支付成功
     */
    void handlePaySuccess(String outTradeNo, String transactionId);
}
