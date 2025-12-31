package com.yxb.wechat.domain.vo;

import lombok.Data;

@Data
public class JssdkSignResponse {
    private String appId;
    private Long timestamp;
    private String nonceStr;
    private String signature;
}
