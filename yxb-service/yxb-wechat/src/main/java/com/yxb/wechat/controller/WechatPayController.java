package com.yxb.wechat.controller;

import com.yxb.common.core.result.Result;
import com.yxb.wechat.biz.WechatPayBiz;
import com.yxb.wechat.domain.vo.PayOrderRequest;
import com.yxb.wechat.domain.vo.PayOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信支付接口
 */
@Tag(name = "微信支付接口")
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class WechatPayController {

    private final WechatPayBiz wechatPayBiz;

    @Operation(summary = "创建支付订单")
    @PostMapping("/create")
    public Result<PayOrderResponse> createOrder(@Valid @RequestBody PayOrderRequest request) {
        return Result.success(wechatPayBiz.createOrder(request));
    }

    @Operation(summary = "支付回调通知")
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) {
        return wechatPayBiz.handlePayNotify(request);
    }

    @Operation(summary = "查询订单状态")
    @GetMapping("/query/{orderNo}")
    public Result<String> queryOrder(@PathVariable String orderNo) {
        return Result.success(wechatPayBiz.queryOrder(orderNo));
    }
}
