package com.lin.bulter.wxplatement.business.weixin.service;

import com.lin.bulter.wxplatement.business.weixin.dto.WeixinAppConfig;
import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.MpMsgReceiveHandler;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

/**
 * 微信公众号服务
 *
 * @author wangchenglin13
 * @date 2020-02-12 11:00
 */
public interface WeixinBaseMpService {

    /**
     * 通过appId，获得配置信息
     * @param appId
     * @return
     */
    WeixinAppConfig getAppConfigByAppid(String appId);

    /**
     * 获得当前公众号配置信息
     * @param appId
     * @return
     */
    WxMpConfigStorage getWxMpConfigStorage(String appId);

    /**
     * 获得accessToken
     * @param appId
     * @return
     */
    String getAccessToken(String appId);

    /**
     * 获得jsTicket
     * @param appId
     * @return
     */
    String getJsTicket(String appId);

    /**
     * 验证微信服务器配置
     * @param appId
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    boolean checkSignature(String appId, String timestamp, String nonce, String signature);

    /**
     * 解密微信的加密消息
     * @param appId
     * @param encryptMsgXml
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    WxMpXmlMessage decryptWxMsgXml(String appId, String encryptMsgXml, String timestamp, String nonce, String signature);

    /**
     * 创建调用jsapi时所需要的签名.
     * @param appId
     * @param url
     * @return
     */
    WxJsapiSignature createJsapiSignature(String appId, String url);

    /**
     * 查询菜单
     * @param appId
     * @return
     */
    WxMpMenu searchMenuList(String appId);

    /**
     * 发布菜单
     * @param appId  公众号Id
     * @param menuJson  菜单Json字符串
     * @return
     */
    boolean deployMenu(String appId, String menuJson);

    /**
     * 发送客服消息
     * @param appId 公众号Id
     * @param kefuMessage 消息
     * @retuen
     */
    boolean sendComtumMsg(String appId, WxMpKefuMessage kefuMessage);

    /**
     * 发送模板消息
     * @param appId 公众号Id
     * @param templateMessage
     * @return 消息Id
     */
    String sendTemplateMsg(String appId, WxMpTemplateMessage templateMessage);

    /**
     * 生成二维码
     * @param appId 公众号Id
     * @param sceneStr  场景值
     * @return  生成的二维码图片Url
     */
    String createQrcode(String appId, String sceneStr);

    /**
     * 获得图文列表
     * @param appId 公众号Id
     * @param pageNo
     * @param pageSize
     * @return
     */
    WxMpMaterialNewsBatchGetResult getMaterialNewsList(String appId, Integer pageNo, Integer pageSize);

    /**
     * 获得素材详情
     * @param appId 公众号Id
     * @param mediaId
     * @return
     */
    WxMpMaterialNews getMaterialNewsInfo(String appId, String mediaId);

    /**
     * 处理微信消息
     * @param appId 公众号Id
     * @param wxMpXmlMessage    微信消息
     * @return
     */
    WxMpXmlOutMessage processWxMessage(String appId, WxMpXmlMessage wxMpXmlMessage, MpMsgReceiveHandler msgReceiveHandler);
}
