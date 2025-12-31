package com.yxb.wechat.controller;

import com.yxb.common.core.result.Result;
import com.yxb.wechat.biz.WechatMpBiz;
import com.yxb.wechat.domain.vo.JssdkSignResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "微信公众号接口")
@RestController
@RequestMapping("/mp")
@RequiredArgsConstructor
public class WechatMpController {

    private final WechatMpBiz wechatMpBiz;

    @Operation(summary = "获取JS-SDK签名")
    @GetMapping("/jssdk/sign")
    public Result<JssdkSignResponse> getJssdkSign(@RequestParam String url) {
        return Result.success(wechatMpBiz.getJssdkSign(url));
    }
}
