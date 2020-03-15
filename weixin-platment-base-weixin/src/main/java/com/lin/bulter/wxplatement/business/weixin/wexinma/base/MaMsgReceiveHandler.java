package com.lin.bulter.wxplatement.business.weixin.wexinma.base;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * 微信小程序消息回调
 *
 * @author wangchenglin13
 * @date 2020-02-12 16:35
 */
public interface MaMsgReceiveHandler {

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
    String handlerCommonMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 关注事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    String handlerSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 取消关注事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    String handlerUnSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage);

    /**
     * 扫描二维码事件
     * @param appId
     * @param wxMpXmlMessage
     * @param isSubscribe  是否关注公众号
     * @return
     */
    String handlerScanQrcodeMsg(String appId, WxMpXmlMessage wxMpXmlMessage, boolean isSubscribe);

    /**
     * 菜单事件
     * @param appId
     * @param wxMpXmlMessage
     * @return
     */
    String handlerMenuMsg(String appId, WxMpXmlMessage wxMpXmlMessage);
}
