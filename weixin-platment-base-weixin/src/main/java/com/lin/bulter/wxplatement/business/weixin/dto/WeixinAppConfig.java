package com.lin.bulter.wxplatement.business.weixin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信应用配置类
 *
 * @author wangchenglin13
 * @date 2020-03-10 18:10
 */
@Data
@NoArgsConstructor
public class WeixinAppConfig {

    /**
     * 微信appid
     */
    private String appId;

    /**
     * 微信应用类型，1：公众号  2：小程序
     */
    private Integer appType;

    /**
     * 微信appsecret
     */
    private String appSecret;

    /**
     * 微信token
     */
    private String appToken;

    /**
     * 应用appid
     */
    private Integer wqAppid;

    /**
     * 应用sid
     */
    private String wqSid;

    /**
     * h5 appId
     */
    private Integer h5Appid;

    /**
     * 0：无效；1：有效
     */
    private Integer status;

}
