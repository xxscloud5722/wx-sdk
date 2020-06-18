package com.github.xxscloud5722.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;

@Data
public class TemplateInfo {
    @JSONField(name = "touser")
    private String openId;
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    @JSONField(name = "miniprogram")
    private MiniProgram miniProgram;
    private HashMap<String, TemplateData> data;


    @Data
    public static class TemplateData {
        private String value;
        private String color;
    }


    @Data
    public static class MiniProgram {
        @JSONField(name = "appId")
        private String appId;

        @JSONField(name = "pagePath")
        private String pagePath;
    }
}
