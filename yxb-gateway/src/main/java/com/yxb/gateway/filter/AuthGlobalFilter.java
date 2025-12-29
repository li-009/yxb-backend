package com.yxb.gateway.filter;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONUtil;
import com.yxb.gateway.config.GatewayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthGlobalFilter {

    private final GatewayConfig gatewayConfig;

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路由
                .addInclude("/**")
                // 排除白名单
                .addExclude(gatewayConfig.getUrls().toArray(new String[0]))
                // 认证函数
                .setAuth(obj -> {
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                })
                // 异常处理
                .setError(e -> {
                    SaResult result = SaResult.error(e.getMessage()).setCode(401);
                    return JSONUtil.toJsonStr(result);
                });
    }
}
