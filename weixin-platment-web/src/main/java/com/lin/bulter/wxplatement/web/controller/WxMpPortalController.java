package com.lin.bulter.wxplatement.web.controller;

import com.lin.bulter.wxplatement.business.service.WeixinMpService;
import com.lin.bulter.wxplatement.business.weixin.service.WeixinBaseMpService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信公众号认证
 *
 * @author wangchenglin13
 * @date 2020-02-12 10:00
 */
@Slf4j
@RestController
@RequestMapping("/wxplatment/portal/{appid}")
public class WxMpPortalController {

    @Autowired
    private WeixinBaseMpService weixinBaseMpService;
    @Autowired
    private WeixinMpService weixinMpService;

    /**
     * 验证微信服务器配置
     * 用于接收 get 参数，返回验证参数
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String checkSignatureGet(
        @PathVariable(name = "appid", required = true) String appid,
        @RequestParam(name = "signature", required = false) String signature,
        @RequestParam(name = "timestamp", required = false) String timestamp,
        @RequestParam(name = "nonce", required = false) String nonce,
        @RequestParam(name = "echostr", required = false) String echostr) {
        try {
            log.info("接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            // 验证
            Boolean checkFlag = weixinBaseMpService.checkSignature(appid, timestamp, nonce, signature);
            if(checkFlag){
                return echostr;
            }
            return "非法请求2";
        } catch (Exception e) {
            log.error("wxmp/checkSignature Exception", e);
        }
        return null;
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String checkSignaturePost(
        @PathVariable(name = "appid", required = true) String appid,
        @RequestBody String requestBody,
        @RequestParam("signature") String signature,
        @RequestParam("timestamp") String timestamp,
        @RequestParam("nonce") String nonce,
        @RequestParam("openid") String openid,
        @RequestParam(name = "encrypt_type", required = false) String encType,
        @RequestParam(name = "msg_signature", required = false) String msgSignature,
        HttpServletResponse response) {
        log.info("WechatEventController->checkSignaturePost, 接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}] timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
            openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!weixinBaseMpService.checkSignature(appid, timestamp, nonce, signature)) {
            log.error("WechatEventController->checkSignaturePost, 接受非法微信请求，可能属于伪造的请求！");
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String result = "";
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            // 消息处理
            WxMpXmlOutMessage outMessage = weixinMpService.processWxMessage(appid, inMessage);
            if (outMessage != null) {
                result = outMessage.toXml();
            }
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = weixinBaseMpService.decryptWxMsgXml(appid, requestBody, timestamp, nonce, signature);
            log.debug("WechatEventController->checkSignaturePost, 消息解密后内容为：{} ", inMessage.toString());
            // 消息处理
            WxMpXmlOutMessage outMessage = weixinMpService.processWxMessage(appid, inMessage);
            if (outMessage != null) {
                result = outMessage.toEncryptedXml(weixinBaseMpService.getWxMpConfigStorage(appid));
            }
        }
        log.info("WechatEventController->checkSignaturePost, 返回消息：{}", result);
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
