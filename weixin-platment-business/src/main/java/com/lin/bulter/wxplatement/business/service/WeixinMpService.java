package com.lin.bulter.wxplatement.business.service;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 微信公众号服务
 *
 * @author wangchenglin13
 * @date 2020-03-10 18:57
 */
public interface WeixinMpService {

    /**
     * 处理微信公众号消息
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage processWxMessage(String appId, WxMpXmlMessage wxMpXmlMessage);
}
