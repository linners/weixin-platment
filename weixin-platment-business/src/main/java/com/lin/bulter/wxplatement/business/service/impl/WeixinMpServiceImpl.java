package com.lin.bulter.wxplatement.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.lin.bulter.wxplatement.business.service.WeixinMpService;
import com.lin.bulter.wxplatement.business.service.msghandler.CommonMpMsgReceiveHandler;
import com.lin.bulter.wxplatement.business.weixin.service.WeixinBaseMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信公众号服务实现
 *
 * @author wangchenglin13
 * @date 2020-03-10 18:58
 */
@Service
public class WeixinMpServiceImpl implements WeixinMpService {
    private static final Logger log = LoggerFactory.getLogger(WeixinMpServiceImpl.class);

    @Autowired
    private CommonMpMsgReceiveHandler commonMpMsgReceiveHandler;
    @Autowired
    private WeixinBaseMpService weixinBaseMpService;

    @Override
    public WxMpXmlOutMessage processWxMessage(String appId, WxMpXmlMessage wxMpXmlMessage) {
        log.info("WeixinMpServiceImpl->processWxMessage start, appId = {}, wxMpXmlMessage = {}", appId, JSON.toJSONString(wxMpXmlMessage));
        // 处理消息
        return weixinBaseMpService.processWxMessage(appId, wxMpXmlMessage, commonMpMsgReceiveHandler);
    }
}
