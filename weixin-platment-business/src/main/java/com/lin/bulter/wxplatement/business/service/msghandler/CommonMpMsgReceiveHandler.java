package com.lin.bulter.wxplatement.business.service.msghandler;

import com.alibaba.fastjson.JSON;
import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.MpMsgReceiveHandler;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 微信消息处理Handler
 *
 * @author wangchenglin13
 * @date 2020-02-12 19:46
 */
@Service
public class CommonMpMsgReceiveHandler implements MpMsgReceiveHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonMpMsgReceiveHandler.class);

    @Override
    public boolean interception(String appId) {
        return false;
    }

    @Override
    public WxMpXmlOutMessage handlerCommonMsg(String appId, WxMpXmlMessage wxMpXmlMessage) {
        log.info("CommonMpMsgReceiveHandler->handlerCommonMsg start, appId = {}, wxMpXmlMessage = {}", appId, JSON.toJSONString(wxMpXmlMessage));

        WxMpXmlOutMessage resultMsg = WxMpXmlOutMessage.TEXT().content("你好").build();
        log.info("CommonMpMsgReceiveHandler->handlerCommonMsg end, appId = {}, wxMpXmlMessage = {}, resultMsg={}", appId, wxMpXmlMessage.getContent(), resultMsg);

        return resultMsg;
    }

    @Override
    public WxMpXmlOutMessage handlerSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage) {
        log.info("CommonMpMsgReceiveHandler->handlerSubscribeMsg start, appId = {}, wxMpXmlMessage = {}", appId, JSON.toJSONString(wxMpXmlMessage));
        WxMpXmlOutMessage resultMsg = WxMpXmlOutMessage.TEXT().content("你好").build();

        return resultMsg;
    }

    @Override
    public WxMpXmlOutMessage handlerUnSubscribeMsg(String appId, WxMpXmlMessage wxMpXmlMessage) {
        log.info("CommonMpMsgReceiveHandler->handlerUnSubscribeMsg start, appId = {}, wxMpXmlMessage = {}", appId, JSON.toJSONString(wxMpXmlMessage));
        WxMpXmlOutMessage resultMsg = WxMpXmlOutMessage.TEXT().content("你好").build();

        return resultMsg;
    }

    @Override
    public WxMpXmlOutMessage handlerScanQrcodeMsg(String appId, WxMpXmlMessage wxMpXmlMessage, boolean isSubscribe) {
        log.info("CommonMsgReceiveHandler->handlerScanQrcodeMsg start, appId={}, wxMpXmlMessage={}, isSubscribe={}", appId, JSON.toJSONString(wxMpXmlMessage), isSubscribe);
        WxMpXmlOutMessage resultMsg = WxMpXmlOutMessage.TEXT().content("你好").build();

        return resultMsg;
    }

    @Override
    public WxMpXmlOutMessage handlerMenuMsg(String appId, WxMpXmlMessage wxMpXmlMessage) {
        log.info("CommonMpMsgReceiveHandler->handlerMenuMsg start, appId = {}, wxMpXmlMessage = {}", appId, JSON.toJSONString(wxMpXmlMessage));
        WxMpXmlOutMessage resultMsg = WxMpXmlOutMessage.TEXT().content("你好").build();
        try {
            // 通过event找的对应的菜单

        }catch (Exception e){
            log.error("菜单点击事件处理异常，appId： {}, 错误信息：{}", appId, e);
        }
        return resultMsg;
    }

}
