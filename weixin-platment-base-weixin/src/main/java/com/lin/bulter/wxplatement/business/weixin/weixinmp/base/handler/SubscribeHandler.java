package com.lin.bulter.wxplatement.business.weixin.weixinmp.base.handler;

import com.alibaba.fastjson.JSON;
import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.MpMsgReceiveHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 1. 关注事件
 * 2. 未关注扫描二维码
 *
 * @author wangchenglin13
 * @date 2020-02-12 12:30
 */
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {

    /**
     * 消息处理Handler
     */
    private MpMsgReceiveHandler msgReceiveHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        log.debug("SubscribeHandler->handle start, wxMessage = {}", JSON.toJSONString(wxMessage));
        // 获得回调函数
        msgReceiveHandler = (MpMsgReceiveHandler) context.get("callBackFunc");
        // 获取appId
        WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
        String appId = wxMpConfigStorage.getAppId();
        // 获取消息信息
        String ticket = wxMessage.getTicket();
        WxMpXmlOutMessage wxMpXmlOutMessage = null;
        // 未关注，扫描二维码
        if(StringUtils.isNotBlank(ticket)){
            wxMpXmlOutMessage = msgReceiveHandler.handlerScanQrcodeMsg(appId, wxMessage, false);
        }
        // 关注事件
        else {
            wxMpXmlOutMessage = msgReceiveHandler.handlerSubscribeMsg(appId, wxMessage);
        }
        wxMpXmlOutMessage.setFromUserName(wxMessage.getToUser());
        wxMpXmlOutMessage.setToUserName(wxMessage.getFromUser());
        return wxMpXmlOutMessage;
    }
}
