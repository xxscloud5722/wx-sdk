package com.github.xxscloud5722.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UserInfo {
    @JSONField(name = "openid")
    private String openId;

    @JSONField(name = "nickname")
    private String nickname;

    @JSONField(name = "sex")
    private String sex;

    @JSONField(name = "province")
    private String province;

    @JSONField(name = "city")
    private String city;

    @JSONField(name = "country")
    private String country;

    @JSONField(name = "headimgurl")
    private String avatarId;

    @JSONField(name = "unionid")
    private String unionId;
}
