package com.lin.bulter.wxplatement.business.weixin.weixinmp.base;

import com.lin.bulter.wxplatement.business.weixin.weixinmp.base.handler.*;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import com.lin.bulter.wxplatement.repository.redis.RedisService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * 公众号配置信息
 *
 * @author wangchenglin13
 * @date 2020-02-12 15:30
 */
public class WxMpConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WxMpConfiguration.class);

	private RedisService redisService;

	public WxMpConfiguration(RedisService redisService) {
		this.redisService = redisService;
	}

	/**
	 * handler注册
 	 */
	private KfSessionHandler kfSessionHandler = new KfSessionHandler();
	private LogHandler logHandler = new LogHandler();
	private NullHandler nullHandler = new NullHandler();
	private StoreCheckNotifyHandler storeCheckNotifyHandler = new StoreCheckNotifyHandler();
	private LocationHandler locationHandler = new LocationHandler();
	private MenuHandler menuHandler = new MenuHandler();
	private MsgHandler msgHandler = new MsgHandler();
	private UnsubscribeHandler unsubscribeHandler = new UnsubscribeHandler();
	private SubscribeHandler subscribeHandler = new SubscribeHandler();
	private ScanHandler scanHandler = new ScanHandler();

	/**
	 * WxMpService
 	 */
	private WxMpService wxMpService;
	/**
	 * wxMpMessageRouter
 	 */
	private WxMpMessageRouter wxMpMessageRouter;

	/**
	 * 获得wxMpService
	 *
	 * @return
	 */
	public WxMpService getWxMpService() {
		return wxMpService;
	}

	/**
	 * 获得消息路由WxMpMessageRouter
	 *
	 * @return
	 */
	public WxMpMessageRouter getWxMpMessageRouter() {
		return wxMpMessageRouter;
	}

	/**
	 * 初始化配置
	 *
	 * @param wechatApps
	 */
	public void init(List<WechatApp> wechatApps) {
		if (CollectionUtils.isNotEmpty(wechatApps)) {
			// 组装配置信息
			Map<String, WxMpConfigStorage> configStorageMap = new HashMap<>(wechatApps.size());
			for (WechatApp wechatApp : wechatApps) {
				if(wechatApp.getAppType()!=null && wechatApp.getAppType() == 2){
					continue;
				}
				// 将配置放入configStorage
				MyWxMpRedisConfigImpl wxMpRedisConfig = new MyWxMpRedisConfigImpl(redisService);
				wxMpRedisConfig.setAppId(wechatApp.getAppId());
				wxMpRedisConfig.setSecret(wechatApp.getAppSecret());
				wxMpRedisConfig.setToken(wechatApp.getAppToken());
				configStorageMap.put(wechatApp.getAppId(), wxMpRedisConfig);
			}
			// 创建wxMpService对象
			wxMpService = new WxMpServiceHttpClientImpl();
			// 微信公众号配置
			wxMpService.setMultiConfigStorages(configStorageMap);
			// 注册消息路由
			wxMpMessageRouter = this.messageRouter();
		} else {
			log.error("WeixinMpDaoImpl->initAppConfig error, 未加载到wechat app配置！");
		}
	}

	/**
	 * 创建消息路由对象
	 *
	 * @return
	 */
	private WxMpMessageRouter messageRouter() {
		final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

		// 记录所有事件的日志 （异步执行）
//		newRouter.rule().handler(this.logHandler).next();

		// 接收客服会话管理事件
		newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
		newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
		newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();

		// 门店审核事件
		newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

		// 自定义菜单事件
		newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();

		// 点击菜单连接事件
		newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();

		// 关注事件
		newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

		// 取消关注事件
		newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

		// 上报地理位置事件
		newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

		// 接收地理位置消息
		newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

		// 扫码事件
		newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

		// 默认
		newRouter.rule().async(false).handler(this.msgHandler).end();

		return newRouter;
	}
}
