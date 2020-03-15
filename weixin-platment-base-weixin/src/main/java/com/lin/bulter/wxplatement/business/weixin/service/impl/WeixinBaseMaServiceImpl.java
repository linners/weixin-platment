package com.lin.bulter.wxplatement.business.weixin.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import com.lin.bulter.wxplatement.business.weixin.service.WeixinBaseMaService;
import com.lin.bulter.wxplatement.business.weixin.wexinma.base.WxMaConfiguration;
import com.lin.bulter.wxplatement.repository.mysql.dao.WechatAppMapper;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import com.lin.bulter.wxplatement.repository.redis.RedisLockService;
import com.lin.bulter.wxplatement.repository.redis.RedisService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 小程序服务
 *
 * @author wangchenglin13
 * @date 2020/3/9 15:59:33
 */
@Service
public class WeixinBaseMaServiceImpl implements WeixinBaseMaService {

	@Autowired
	private RedisService redisService;
	@Autowired
	private RedisLockService redisLockService;
	@Autowired
	private WechatAppMapper wechatAppMapper;

	private WxMaConfiguration wxMaConfiguration;


	@PostConstruct
	private void initAppConfig(){
		// 查询wechatAppList
		List<WechatApp> wechatApps = wechatAppMapper.selectAll();
		// 查询配置信息
		if(CollectionUtils.isNotEmpty(wechatApps)){
			wxMaConfiguration = new WxMaConfiguration(redisService, redisLockService);
			wxMaConfiguration.init(wechatApps);
		}
	}

	@Override
	public WxMaJscode2SessionResult code2Session(String appid, String code){
		WxMaService wxMaService = wxMaConfiguration.getWxMaService(appid);
		try {
			WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(code);
			return result;
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean pushTemplateMsg(String appid, WxMaUniformMessage uniformMessage) {
		WxMaService wxMaService = wxMaConfiguration.getWxMaService(appid);
		try {
			wxMaService.getMsgService().sendUniformMsg(uniformMessage);
			return true;
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return false;
	}
}
