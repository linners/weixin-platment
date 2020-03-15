package com.lin.bulter.wxplatement.business.weixin.weixinmp.base;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 微信公众号消息回调
 *
 * @author wangchenglin13
 * @date 2020-02-12 16:35
 */
public interface MpMsgReceiveHandler {

    /**
     * 过滤处理的handler
     * @param appId
     * @return
     */
    boolean interception(String appId);

    /**
     * 普通消息事件
     * 包括：关键词搜索自动回复
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage handlerCommonMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 关注事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage handlerSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 取消关注事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage handlerUnSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 扫描二维码事件
     * @param appId
     * @param wxMpXmlMessage
     * @param isSubscribe  是否关注公众号
     * @return
     */
    WxMpXmlOutMessage handlerScanQrcodeMsg(String appId, WxMpXmlMessage wxMpXmlMessage, boolean isSubscribe);

    /**
     * 菜单事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage handlerMenuMsg(String appId, WxMpXmlMessage wxMpXmlMessage);
}
