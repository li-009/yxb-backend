package com.yxb.wechat.biz;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.util.UserContextHolder;
import com.yxb.wechat.domain.vo.PayOrderRequest;
import com.yxb.wechat.domain.vo.PayOrderResponse;
import com.yxb.wechat.service.WechatPayOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 微信支付业务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayBiz {

    private final WxPayService wxPayService;
    private final WechatPayOrderService wechatPayOrderService;

    /**
     * 创建支付订单
     */
    public PayOrderResponse createOrder(PayOrderRequest request) {
        try {
            Long userId = UserContextHolder.getUserId();
            String openid = UserContextHolder.getOpenId();
            
            WxPayUnifiedOrderRequest orderRequest = WxPayUnifiedOrderRequest.newBuilder()
                    .outTradeNo(request.getOrderNo())
                    .totalFee(request.getAmount())
                    .body(request.getDescription())
                    .tradeType(WxPayConstants.TradeType.JSAPI)
                    .openid(openid)
                    .spbillCreateIp(request.getClientIp())
                    .build();
            
            WxPayMpOrderResult result = wxPayService.createOrder(orderRequest);
            
            PayOrderResponse response = new PayOrderResponse();
            response.setAppId(result.getAppId());
            response.setTimeStamp(result.getTimeStamp());
            response.setNonceStr(result.getNonceStr());
            response.setPackageValue(result.getPackageValue());
            response.setSignType(result.getSignType());
            response.setPaySign(result.getPaySign());
            
            return response;
        } catch (WxPayException e) {
            log.error("创建支付订单失败: {}", e.getMessage(), e);
            throw new BizException("创建支付订单失败: " + e.getMessage());
        }
    }

    /**
     * 处理支付回调通知
     */
    public String handlePayNotify(HttpServletRequest request) {
        try {
            String xmlData = readXmlFromRequest(request);
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            
            if (WxPayConstants.ResultCode.SUCCESS.equals(notifyResult.getResultCode())) {
                String outTradeNo = notifyResult.getOutTradeNo();
                String transactionId = notifyResult.getTransactionId();
                
                wechatPayOrderService.handlePaySuccess(outTradeNo, transactionId);
                
                log.info("支付成功回调处理完成, 订单号: {}", outTradeNo);
            }
            
            return WxPayNotifyResponse.success("成功");
        } catch (WxPayException e) {
            log.error("支付回调处理失败: {}", e.getMessage(), e);
            return WxPayNotifyResponse.fail("处理失败");
        }
    }

    /**
     * 查询订单状态
     */
    public String queryOrder(String orderNo) {
        try {
            WxPayOrderQueryResult result = wxPayService.queryOrder(null, orderNo);
            return result.getTradeState();
        } catch (WxPayException e) {
            log.error("查询订单失败: {}", e.getMessage(), e);
            throw new BizException("查询订单失败: " + e.getMessage());
        }
    }

    private String readXmlFromRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("读取请求数据失败", e);
        }
        return sb.toString();
    }
}
