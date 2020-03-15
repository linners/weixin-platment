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

import java.util.Map;

/**
 * 用户已关注时,扫描二维码事件
 *
 * @author wangchenglin13
 * @date 2020-02-12 12:30
 */
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    /**
     * 消息处理Handler
     */
    private MpMsgReceiveHandler msgReceiveHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        log.debug("ScanHandler->handle start, wxMessage = {}", JSON.toJSONString(wxMessage));
        // 获得回调函数
        msgReceiveHandler = (MpMsgReceiveHandler) context.get("callBackFunc");
        // 获取appId
        WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
        String appId = wxMpConfigStorage.getAppId();
        WxMpXmlOutMessage wxMpXmlOutMessage = msgReceiveHandler.handlerScanQrcodeMsg(appId, wxMessage, true);
        wxMpXmlOutMessage.setFromUserName(wxMessage.getToUser());
        wxMpXmlOutMessage.setToUserName(wxMessage.getFromUser());
        return wxMpXmlOutMessage;
    }
}
