package com.lin.bulter.wxplatement.business.weixin.weixinmp.base;

import com.lin.bulter.wxplatement.repository.redis.RedisService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.enums.TicketType;

/**
 * 公众号，redis存储配置信息
 *
 * @author wangchenglin13
 * @date 2020-02-11 18:23
 */
public class MyWxMpRedisConfigImpl extends WxMpDefaultConfigImpl {
	private static final String ACCESS_TOKEN_KEY = "wx:access_token:";
	private static final String TICKET_KEY = "wx:ticket:";

	private final RedisService redisService;

	public MyWxMpRedisConfigImpl(RedisService redisService) {
		this.redisService = redisService;
	}

	/**
	 * 获取accessToken redisKey
	 *
	 * @return
	 */
	private String getAccessTokenRedisKey() {
		return this.appId + this.secret;
	}

	/**
	 * 获取ticket redisKey
	 *
	 * @param type
	 * @return
	 */
	private String getTicketRedisKey(TicketType type) {
		return String.format(TICKET_KEY.concat(":key:%s:%s"), this.appId, type.getCode());
	}

	@Override
	public String getAccessToken() {
		Object redisVal = redisService.get(this.getAccessTokenRedisKey());
		return redisVal != null ? redisVal.toString() : "";
	}

	@Override
	public boolean isAccessTokenExpired() {
		return redisService.getExpire(this.getAccessTokenRedisKey()) < 2L;
	}

	@Override
	public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
		redisService.set(this.getAccessTokenRedisKey(), accessToken, expiresInSeconds - 200);

	}

	@Override
	public void expireAccessToken() {
		redisService.expire(this.getAccessTokenRedisKey(), 0);
	}

	@Override
	public String getTicket(TicketType type) {
		Object redisVal = redisService.get(this.getTicketRedisKey(type));
		return redisVal != null ? redisVal.toString() : "";
	}

	@Override
	public boolean isTicketExpired(TicketType type) {
		return redisService.getExpire(this.getTicketRedisKey(type)) < 2L;
	}

	@Override
	public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
		redisService.set(this.getTicketRedisKey(type), jsapiTicket, expiresInSeconds - 200);
	}

	@Override
	public void expireTicket(TicketType type) {
		redisService.expire(this.getTicketRedisKey(type), 0);
	}
}
