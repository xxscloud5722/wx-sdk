package com.github.xxscloud5722.wechat;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class WeChatSDK {

    private final String appId;
    private final String secret;
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    public WeChatSDK(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    /**
     * 获取AccessToken.
     *
     * @return 令牌.
     * @throws IOException 获取数据异常.
     */
    public String getAccessToken() throws IOException {
        final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
                appId + "&secret=" + secret;
        return get(url).getString("access_token");
    }

    /**
     * 获取用户信息.
     *
     * @param code 公众号代码.
     * @return 用户信息.
     * @throws IOException 获取数据异常
     */
    public UserInfo getUserInfoByPublicCode(String code) throws IOException {
        final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&code=" +
                code + "&appid=" + appId + "&secret=" + secret;
        final JSONObject r1 = get(url);

        final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + r1.getString("access_token")
                + "&openid=" + r1.getString("openid") + "&lang=zh_CN";
        return get(userInfoUrl, UserInfo.class);
    }

    /**
     * 获取用户信息.
     *
     * @param templateInfo 消息.
     * @return 用户信息.
     * @throws IOException 获取数据异常
     */
    public boolean sendTemplateMessage(String accessToken, TemplateInfo templateInfo) throws IOException {
        if (StringUtils.isEmpty(templateInfo.getTemplateId())) {
            throw new IOException("templateId is null");
        }
        if (StringUtils.isEmpty(templateInfo.getOpenId())) {
            throw new IOException("templateId is openId");
        }
        if (ObjectUtils.isEmpty(templateInfo.getData())) {
            throw new IOException("templateId is data");
        }
        final String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        return post(url, templateInfo) != null;
    }

    private JSONObject get(String url) throws IOException {
        return get(url, JSONObject.class);
    }

    private <T> T get(String url, Class<T> clazz) throws IOException {
        final HttpGet httpGet = new HttpGet(url);
        log.info("[微信SDK] 请求地址: " + url);
        final HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("response code : " + response.getStatusLine().getStatusCode());
        }
        final String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
        log.info("[微信SDK] 响应: " + responseString);

        final JSONObject result = JSONObject.parseObject(responseString);
        if (!Objects.equals(result.getString("errcode"), "0")) {
            throw new IOException(result.getString("errmsg"));
        }
        return JSONObject.parseObject(responseString, clazz);
    }

    private JSONObject post(String url, Object body) throws IOException {
        return post(url, body, JSONObject.class);
    }

    private <T> T post(String url, Object body, Class<T> clazz) throws IOException {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(body)));
        log.info("[微信SDK] 请求地址: " + url);
        final HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("response code : " + response.getStatusLine().getStatusCode());
        }
        final String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
        log.info("[微信SDK] 响应: " + responseString);

        final JSONObject result = JSONObject.parseObject(responseString);
        if (!Objects.equals(result.getString("errcode"), "0")) {
            throw new IOException(result.getString("errmsg"));
        }
        return JSONObject.parseObject(responseString, clazz);
    }
}
