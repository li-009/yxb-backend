package com.yxb.wechat.biz;

import com.yxb.wechat.config.WechatMpConfig;
import com.yxb.wechat.domain.vo.JssdkSignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WechatMpBiz {

    private final WechatMpConfig wechatMpConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    
    private String accessToken;
    private long accessTokenExpireTime;
    private String jsapiTicket;
    private long jsapiTicketExpireTime;

    public JssdkSignResponse getJssdkSign(String url) {
        String ticket = getJsapiTicket();
        String nonceStr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        long timestamp = System.currentTimeMillis() / 1000;
        
        String string1 = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + 
                         "&timestamp=" + timestamp + "&url=" + url;
        String signature = sha1(string1);
        
        JssdkSignResponse response = new JssdkSignResponse();
        response.setAppId(wechatMpConfig.getAppid());
        response.setTimestamp(timestamp);
        response.setNonceStr(nonceStr);
        response.setSignature(signature);
        return response;
    }
    
    private String getAccessToken() {
        if (accessToken != null && System.currentTimeMillis() < accessTokenExpireTime) {
            return accessToken;
        }
        
        String url = String.format(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            wechatMpConfig.getAppid(), wechatMpConfig.getSecret()
        );
        
        try {
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            if (result != null && result.containsKey("access_token")) {
                accessToken = (String) result.get("access_token");
                int expiresIn = (Integer) result.get("expires_in");
                accessTokenExpireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
                return accessToken;
            }
        } catch (Exception e) {
            log.error("获取access_token失败", e);
        }
        return null;
    }
    
    private String getJsapiTicket() {
        if (jsapiTicket != null && System.currentTimeMillis() < jsapiTicketExpireTime) {
            return jsapiTicket;
        }
        
        String token = getAccessToken();
        if (token == null) return null;
        
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
        
        try {
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            if (result != null && result.containsKey("ticket")) {
                jsapiTicket = (String) result.get("ticket");
                int expiresIn = (Integer) result.get("expires_in");
                jsapiTicketExpireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
                return jsapiTicket;
            }
        } catch (Exception e) {
            log.error("获取jsapi_ticket失败", e);
        }
        return null;
    }
    
    private String sha1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("SHA1加密失败", e);
            return null;
        }
    }
}
