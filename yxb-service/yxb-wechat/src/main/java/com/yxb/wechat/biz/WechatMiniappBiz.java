package com.yxb.wechat.biz;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.yxb.common.core.exception.BizException;
import com.yxb.wechat.domain.vo.MiniappLoginRequest;
import com.yxb.wechat.domain.vo.MiniappLoginResponse;
import com.yxb.wechat.domain.vo.MiniappPhoneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/**
 * 微信小程序业务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMiniappBiz {

    private final WxMaService wxMaService;

    /**
     * 小程序登录
     */
    public MiniappLoginResponse login(MiniappLoginRequest request) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(request.getCode());
            
            MiniappLoginResponse response = new MiniappLoginResponse();
            response.setOpenid(session.getOpenid());
            response.setUnionid(session.getUnionid());
            response.setSessionKey(session.getSessionKey());
            
            return response;
        } catch (WxErrorException e) {
            log.error("小程序登录失败: {}", e.getMessage(), e);
            throw new BizException("小程序登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取手机号
     */
    public String getPhoneNumber(MiniappPhoneRequest request) {
        try {
            WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNoInfo(request.getCode());
            return phoneInfo.getPhoneNumber();
        } catch (WxErrorException e) {
            log.error("获取手机号失败: {}", e.getMessage(), e);
            throw new BizException("获取手机号失败: " + e.getMessage());
        }
    }
}
