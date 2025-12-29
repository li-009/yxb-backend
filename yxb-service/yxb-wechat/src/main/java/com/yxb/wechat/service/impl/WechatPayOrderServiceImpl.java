package com.yxb.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxb.wechat.domain.entity.WechatPayOrder;
import com.yxb.wechat.mapper.WechatPayOrderMapper;
import com.yxb.wechat.service.WechatPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 微信支付订单服务实现
 */
@Slf4j
@Service
public class WechatPayOrderServiceImpl extends ServiceImpl<WechatPayOrderMapper, WechatPayOrder> implements WechatPayOrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaySuccess(String outTradeNo, String transactionId) {
        LambdaUpdateWrapper<WechatPayOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WechatPayOrder::getOutTradeNo, outTradeNo)
                .eq(WechatPayOrder::getPayStatus, 0)
                .set(WechatPayOrder::getPayStatus, 1)
                .set(WechatPayOrder::getTransactionId, transactionId)
                .set(WechatPayOrder::getPayTime, LocalDateTime.now());
        
        boolean updated = this.update(updateWrapper);
        if (updated) {
            log.info("支付订单状态更新成功, outTradeNo: {}", outTradeNo);
        } else {
            log.warn("支付订单状态更新失败或已处理, outTradeNo: {}", outTradeNo);
        }
    }
}
