package com.lin.bulter.wxplatement.business.weixin.service;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 微信公众号服务,网页授权
 *
 * @author wangchenglin13
 * @date 2020-02-12 11:00
 */
public interface WeixinBaseMpOauth2Service {

    /**
     * 网页授权，获得请求URl
     * @param appId
     * @param redirectU
     * @param snsapiBase
     * @param state
     * @return
     */
    String oauth2buildAuthorizationUrl(String appId, String redirectU, String snsapiBase, String state);

    /**
     * 通过code，获得OAth
     * @param appId
     * @param code
     * @return
     */
    WxMpOAuth2AccessToken oauth2getAccessToken(String appId, String code);

    /**
     * 获得用户信息
     * @param appId
     * @param oAuth2AccessToken
     * @return
     */
    WxMpUser oauth2getUserInfo(String appId, WxMpOAuth2AccessToken oAuth2AccessToken);
}
