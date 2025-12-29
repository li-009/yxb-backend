package com.yxb.wechat.controller;

import com.yxb.common.core.result.Result;
import com.yxb.wechat.biz.WechatMiniappBiz;
import com.yxb.wechat.domain.vo.MiniappLoginRequest;
import com.yxb.wechat.domain.vo.MiniappLoginResponse;
import com.yxb.wechat.domain.vo.MiniappPhoneRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序接口
 */
@Tag(name = "微信小程序接口")
@RestController
@RequestMapping("/miniapp")
@RequiredArgsConstructor
public class WechatMiniappController {

    private final WechatMiniappBiz wechatMiniappBiz;

    @Operation(summary = "小程序登录")
    @PostMapping("/login")
    public Result<MiniappLoginResponse> login(@Valid @RequestBody MiniappLoginRequest request) {
        return Result.success(wechatMiniappBiz.login(request));
    }

    @Operation(summary = "获取手机号")
    @PostMapping("/phone")
    public Result<String> getPhoneNumber(@Valid @RequestBody MiniappPhoneRequest request) {
        return Result.success(wechatMiniappBiz.getPhoneNumber(request));
    }
}
