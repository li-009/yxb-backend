package com.yxb.user.controller.inner;

import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.result.Result;
import com.yxb.user.biz.UserBiz;
import com.yxb.user.convert.UserConvert;
import com.yxb.user.domain.entity.UserWechat;
import com.yxb.user.service.UserService;
import com.yxb.user.service.UserWechatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户内部接口（服务间调用）
 */
@Tag(name = "用户内部接口")
@RestController
@RequestMapping("/inner/user")
@RequiredArgsConstructor
public class UserInnerController {

    private final UserBiz userBiz;
    private final UserService userService;
    private final UserWechatService userWechatService;

    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        return Result.success(userBiz.getUserById(id));
    }

    @Operation(summary = "根据OpenID获取用户信息")
    @GetMapping("/openid/{openId}")
    public Result<UserDTO> getUserByOpenId(@PathVariable String openId) {
        UserWechat wechat = userWechatService.getByOpenIdMini(openId);
        if (wechat == null) {
            wechat = userWechatService.getByOpenIdApp(openId);
        }
        if (wechat == null) {
            wechat = userWechatService.getByOpenIdMp(openId);
        }
        if (wechat == null) {
            return Result.success(null);
        }
        return Result.success(UserConvert.INSTANCE.toDTO(userService.getById(wechat.getUserId())));
    }

    @Operation(summary = "根据UnionID获取用户信息")
    @GetMapping("/unionid/{unionId}")
    public Result<UserDTO> getUserByUnionId(@PathVariable String unionId) {
        UserWechat wechat = userWechatService.getByUnionId(unionId);
        if (wechat == null) {
            return Result.success(null);
        }
        return Result.success(UserConvert.INSTANCE.toDTO(userService.getById(wechat.getUserId())));
    }

    @Operation(summary = "增加用户积分")
    @PostMapping("/{id}/points/add")
    public Result<Void> addPoints(@PathVariable Long id, @RequestParam Integer points) {
        userBiz.addPoints(id, points);
        return Result.success();
    }

    @Operation(summary = "增加学习时长")
    @PostMapping("/{id}/study-time/add")
    public Result<Void> addStudyTime(@PathVariable Long id, @RequestParam Integer minutes) {
        userBiz.addStudyTime(id, minutes);
        return Result.success();
    }

    @Operation(summary = "检查VIP状态")
    @GetMapping("/{id}/vip/check")
    public Result<Boolean> checkVipStatus(@PathVariable Long id) {
        return Result.success(userBiz.checkVipStatus(id));
    }
}
