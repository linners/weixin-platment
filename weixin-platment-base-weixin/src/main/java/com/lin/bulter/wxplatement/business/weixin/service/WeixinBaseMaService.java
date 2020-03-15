package com.lin.bulter.wxplatement.business.weixin.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;

/**
 * 微信小程序服务
 */
public interface WeixinBaseMaService {

	/**
	 * 登录凭证校验。
	 * 通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程
	 * 注意：该登录凭证过期时间很短。需要小程序端调用wx.checkSession方法，确认凭证是否有效。若无效，需要重新走登录流程
	 * @param appid
	 * @param code
	 * @return
	 */
	WxMaJscode2SessionResult code2Session(String appid, String code);

	/**
	 * 发送模板消息
	 * @param appid
	 * @param uniformMessage
	 */
	boolean pushTemplateMsg(String appid, WxMaUniformMessage uniformMessage);

}
