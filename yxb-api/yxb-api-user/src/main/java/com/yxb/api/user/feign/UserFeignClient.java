package com.yxb.api.user.feign;

import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "yxb-user", contextId = "userFeignClient", path = "/inner/user")
public interface UserFeignClient {

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    Result<UserDTO> getUserById(@PathVariable("id") Long id);

    /**
     * 根据OpenID获取用户信息
     */
    @GetMapping("/openid/{openId}")
    Result<UserDTO> getUserByOpenId(@PathVariable("openId") String openId);

    /**
     * 根据UnionID获取用户信息
     */
    @GetMapping("/unionid/{unionId}")
    Result<UserDTO> getUserByUnionId(@PathVariable("unionId") String unionId);

    /**
     * 增加用户积分
     */
    @PostMapping("/{id}/points/add")
    Result<Void> addPoints(@PathVariable("id") Long id, @RequestParam("points") Integer points);

    /**
     * 更新用户学习时长
     */
    @PostMapping("/{id}/study-time/add")
    Result<Void> addStudyTime(@PathVariable("id") Long id, @RequestParam("minutes") Integer minutes);

    /**
     * 检查用户VIP状态
     */
    @GetMapping("/{id}/vip/check")
    Result<Boolean> checkVipStatus(@PathVariable("id") Long id);
}
