package com.lin.bulter.wxplatement.business.weixin.wexinma.base;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Maps;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import com.lin.bulter.wxplatement.repository.redis.RedisLockService;
import com.lin.bulter.wxplatement.repository.redis.RedisService;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 小程序配置信息
 *
 * @author wangchenglin13
 * @date 2020-02-12 15:30
 */
@NoArgsConstructor
public class WxMaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WxMaConfiguration.class);

	private RedisService redisService;
	private RedisLockService redisLockService;

	// Map<appid, WxMaservice>
	private static Map<String, WxMaService> maServices = Maps.newHashMap();
	// Map<appid, WxMaMessageRouter>
	private static Map<String, WxMaMessageRouter> routers = Maps.newHashMap();

	public WxMaConfiguration(RedisService redisService, RedisLockService redisLockService){
		this.redisService = redisService;
		this.redisLockService = redisLockService;
	}

	/**
	 * 获得小程序对应的wxWaService
	 *
	 * @param appid
	 * @return
	 */
	public WxMaService getWxMaService(String appid) {
		WxMaService wxService = maServices.get(appid);
		if (wxService == null) {
			throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
		}

		return wxService;
	}

	/**
	 * 获得路由对象
	 *
	 * @param appid
	 * @return
	 */
	public WxMaMessageRouter getMessageRouter(String appid) {
		return routers.get(appid);
	}

	/**
	 * 初始化配置
	 */
	public void init(List<WechatApp> wechatApps) {
		if (CollectionUtils.isEmpty(wechatApps)) {
			log.error("WxMaConfiguration->init failed, wechatApps is null");
		}
		for (WechatApp wechatApp : wechatApps) {
			if(wechatApp.getAppType()!=null && wechatApp.getAppType() == 1){
				continue;
			}
			// 微信小程序配置
			MyWxMaRedisConfigImpl config = new MyWxMaRedisConfigImpl(redisService, redisLockService);
			config.setAppid(wechatApp.getAppId());
			config.setSecret(wechatApp.getAppSecret());
			config.setToken(wechatApp.getAppToken());
			//config.setMsgDataFormat(wechatApp.getMsgDataFormat());

			// 小程序服务对象
			WxMaService wxMaService = new WxMaServiceImpl();
			wxMaService.setWxMaConfig(config);
			maServices.put(wechatApp.getAppId(), wxMaService);

			// 路由设置
			routers.put(wechatApp.getAppId(), this.newRouter(wxMaService));
		}
	}

	private WxMaMessageRouter newRouter(WxMaService service) {
		final WxMaMessageRouter router = new WxMaMessageRouter(service);
		router
				.rule().handler(logHandler).next()
				.rule().async(false).content("文本").handler(textHandler).end()
				.rule().async(false).content("图片").handler(picHandler).end()
				.rule().async(false).content("二维码").handler(qrcodeHandler).end();
		return router;
	}

	private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
		System.out.println("收到消息：" + wxMessage.toString());
		service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
				.toUser(wxMessage.getFromUser()).build());
		return null;
	};

	private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
		service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
				.toUser(wxMessage.getFromUser()).build());
		return null;
	};

	private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
		try {
			WxMediaUploadResult uploadResult = service.getMediaService()
					.uploadMedia("image", "png",
							ClassLoader.getSystemResourceAsStream("tmp.png"));
			service.getMsgService().sendKefuMsg(
					WxMaKefuMessage
							.newImageBuilder()
							.mediaId(uploadResult.getMediaId())
							.toUser(wxMessage.getFromUser())
							.build());
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return null;
	};

	private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
		try {
			final File file = service.getQrcodeService().createQrcode("123", 430);
			WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
			service.getMsgService().sendKefuMsg(
					WxMaKefuMessage
							.newImageBuilder()
							.mediaId(uploadResult.getMediaId())
							.toUser(wxMessage.getFromUser())
							.build());
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return null;
	};

}
