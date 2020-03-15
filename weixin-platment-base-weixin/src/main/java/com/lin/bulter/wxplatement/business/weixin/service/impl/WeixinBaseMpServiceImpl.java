package com.lin.bulter.wxplatement.business.weixin.service.impl;

import com.lin.bulter.wxplatement.business.weixin.dto.WeixinAppConfig;
import com.lin.bulter.wxplatement.business.weixin.service.WeixinBaseMpOauth2Service;
import com.lin.bulter.wxplatement.business.weixin.service.WeixinBaseMpService;
import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.MpMsgReceiveHandler;
import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.WxMpConfiguration;
import com.lin.bulter.wxplatement.common.exception.AppBusinessErrorCode;
import com.lin.bulter.wxplatement.common.exception.BusinessException;
import com.lin.bulter.wxplatement.common.utils.BeanUtil;
import com.lin.bulter.wxplatement.repository.mysql.dao.WechatAppMapper;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import com.lin.bulter.wxplatement.repository.redis.RedisService;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号服务实现
 *
 * @author wangchenglin13
 * @date 2020-02-12 11:01
 */
@Service
public class WeixinBaseMpServiceImpl implements WeixinBaseMpService, WeixinBaseMpOauth2Service {

	private static final Logger log = LoggerFactory.getLogger(WeixinBaseMpServiceImpl.class);

	/**
	 * 微信公众号服务
	 */
	private WxMpService wxMpService;
	/**
	 * 消息路由
	 */
	private WxMpMessageRouter wxMpMessageRouter;

	@Autowired
	private RedisService redisService;
	@Autowired
	private WechatAppMapper wechatAppMapper;

	private List<WechatApp> wechatApps;

	@PostConstruct
	private void initAppConfig() {
		// 查询wechatAppList
		wechatApps = wechatAppMapper.selectAll();
		WxMpConfiguration wxMpConfiguration = new WxMpConfiguration(redisService);
		// 查询配置信息
		if (wechatApps != null) {
			// 初始化配置
			wxMpConfiguration.init(wechatApps);
			// 创建微信公众号服务对象
			wxMpService = wxMpConfiguration.getWxMpService();
			// 创建微信公众号消息路由
			wxMpMessageRouter = wxMpConfiguration.getWxMpMessageRouter();
		}
	}

	@Override
	public String oauth2buildAuthorizationUrl(String appId, String redirectUrl, String snsapiBase, String state) {
		return wxMpService.switchoverTo(appId).oauth2buildAuthorizationUrl(redirectUrl, snsapiBase, state);
	}

	@Override
	public WxMpOAuth2AccessToken oauth2getAccessToken(String appId, String code) {
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
		try {
			wxMpOAuth2AccessToken = wxMpService.switchoverTo(appId).oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return wxMpOAuth2AccessToken;
	}

	@Override
	public WxMpUser oauth2getUserInfo(String appId, WxMpOAuth2AccessToken oAuth2AccessToken) {
		try {
			WxMpUser wxMpUser = wxMpService.switchoverTo(appId).oauth2getUserInfo(oAuth2AccessToken, "zh_CN");
			return wxMpUser;
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public WeixinAppConfig getAppConfigByAppid(String appId) {
		if (CollectionUtils.isNotEmpty(wechatApps)) {
			for (WechatApp appConfig : wechatApps) {
				if (appId.equals(appConfig.getAppId())) {
					WeixinAppConfig weixinAppConfig = BeanUtil.copy(appConfig, WeixinAppConfig::new);
					return weixinAppConfig;
				}
			}
		}
		return null;
	}

	@Override
	public WxMpConfigStorage getWxMpConfigStorage(String appId) {
		return wxMpService.switchoverTo(appId).getWxMpConfigStorage();
	}

	@Override
	public String getAccessToken(String appId) {
		try {
			String accessToken = wxMpService.switchoverTo(appId).getAccessToken();
			return accessToken;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->getAccessToken error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public String getJsTicket(String appId) {
		try {
			String jsapiTicket = wxMpService.switchoverTo(appId).getJsapiTicket();
			return jsapiTicket;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->getJsTicket error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public boolean checkSignature(String appId, String timestamp, String nonce, String signature) {
		return wxMpService.switchoverTo(appId).checkSignature(timestamp, nonce, signature);
	}

	@Override
	public WxJsapiSignature createJsapiSignature(String appId, String url) {
		try {
			WxJsapiSignature wxJsapiSignature = wxMpService.switchoverTo(appId).createJsapiSignature(url);
			return wxJsapiSignature;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->createJsapiSignature error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public WxMpMenu searchMenuList(String appId) {
		// 菜单服务
		WxMpMenuService menuService = wxMpService.switchoverTo(appId).getMenuService();
		// 查询菜单
		try {
			WxMpMenu wxMpMenu = menuService.menuGet();
			return wxMpMenu;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->searchMenuList error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public boolean deployMenu(String appId, String menuJson) {
		WxMpMenuService menuService = wxMpService.switchoverTo(appId).getMenuService();
		try {
			menuService.menuCreate(menuJson);
			return true;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->deployMenu error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public boolean sendComtumMsg(String appId, WxMpKefuMessage kefuMessage) {
		WxMpKefuService kefuService = wxMpService.switchoverTo(appId).getKefuService();
		try {
			return kefuService.sendKefuMessage(kefuMessage);
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->sendComtumMsg error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public String sendTemplateMsg(String appId, WxMpTemplateMessage templateMessage) {
		WxMpTemplateMsgService templateMsgService = wxMpService.switchoverTo(appId).getTemplateMsgService();
		try {
			return templateMsgService.sendTemplateMsg(templateMessage);
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->sendTemplateMsg error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public String createQrcode(String appId, String sceneStr) {
		WxMpQrcodeService qrcodeService = wxMpService.switchoverTo(appId).getQrcodeService();
		try {
			// 获取ticket
			WxMpQrCodeTicket wxMpQrCodeTicket = qrcodeService.qrCodeCreateLastTicket(sceneStr);
			// 通过ticket获取二维码
			String url = qrcodeService.qrCodePictureUrl(wxMpQrCodeTicket.getTicket());
			return url;
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->createQrcode error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public WxMpMaterialNewsBatchGetResult getMaterialNewsList(String appId, Integer pageNo, Integer pageSize) {
		WxMpMaterialService materialService = wxMpService.switchoverTo(appId).getMaterialService();
		Integer offset = (pageNo - 1) * pageSize;
		try {
			return materialService.materialNewsBatchGet(offset, pageSize);
		} catch (WxErrorException e) {
			log.error("WeixinMpServiceImpl->getMaterialNewsList error, msg 【{}】", e.getError());
			throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public WxMpMaterialNews getMaterialNewsInfo(String appId, String mediaId) {
		WxMpMaterialService materialService = wxMpService.switchoverTo(appId).getMaterialService();
		try {
			return materialService.materialNewsInfo(mediaId);
		} catch (WxErrorException e) {
        log.error("WeixinMpServiceImpl->getMaterialNewsInfo error, msg 【{}】", e.getError());
        throw new BusinessException(AppBusinessErrorCode.WEIXIN_ERROR);
		}
	}

	@Override
	public WxMpXmlOutMessage processWxMessage(String appId, WxMpXmlMessage wxMpXmlMessage, MpMsgReceiveHandler msgReceiveHandler) {
		// 切换公众号
		wxMpService.switchover(appId);
		// 路由发送消息
		Map<String, Object> context = new HashMap<>(1);
		context.put("callBackFunc", msgReceiveHandler);
		return wxMpMessageRouter.route(wxMpXmlMessage, context);
	}

	@Override
	public WxMpXmlMessage decryptWxMsgXml(String appId, String encryptMsgXml, String timestamp, String nonce, String signature) {
		// 切换公众号
		wxMpService.switchover(appId);
		// 获取配置信息
		WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
		WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(encryptMsgXml, wxMpConfigStorage,
				timestamp, nonce, signature);
		return inMessage;
	}

}
