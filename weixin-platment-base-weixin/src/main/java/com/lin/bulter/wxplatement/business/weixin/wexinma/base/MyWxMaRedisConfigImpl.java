package com.lin.bulter.wxplatement.business.weixin.wexinma.base;

import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.lin.bulter.wxplatement.repository.redis.RedisLockService;
import com.lin.bulter.wxplatement.repository.redis.RedisService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 小程序，redis存储配置信息
 *
 * @author wangchenglin13
 * @date 2020-02-11 18:23
 */
public class MyWxMaRedisConfigImpl extends WxMaDefaultConfigImpl {

	private static final String ACCESS_TOKEN = "accessToken";
	private static final String JSAPI_TICKET = "jsapiTicket";
	private static final String CARD_API_TICKET = "cardApiTicket";

	private static final String HASH_VALUE_FIELD = "value";
	private static final String HASH_EXPIRE_FIELD = "expire";

	/**
	 * Redis Key 的前缀，默认为 maConfig
	 */
	private String redisKeyPrefix = "maConfig";

	/**
	 * 微信小程序唯一id，用于拼接存储到redis时的key，防止key重复.
	 */
	private String maId;

	private Lock accessTokenLock;
	private Lock jsapiTicketLock;
	private Lock cardApiTicketLock;

	private final RedisService redisService;
	private final RedisLockService redisLockService;

	public MyWxMaRedisConfigImpl(RedisService redisService, RedisLockService redisLockService) {
		this.redisService = redisService;
		this.redisLockService = redisLockService;
	}

	private String getRedisKey(String key) {
		StringBuilder redisKey = new StringBuilder(redisKeyPrefix).append(":");
		if (maId == null) {
			return redisKey.append(key).toString();
		} else {
			return redisKey.append(maId).append(":").append(key).toString();
		}
	}

	private String getValueFromRedis(String key) {
		Object redisVal = redisService.hget(getRedisKey(key), HASH_VALUE_FIELD);
		return redisVal != null ? redisVal.toString() : "";
	}

	private void setValueToRedis(String key, long expiresTime, String value) {
		redisService.hset(getRedisKey(key), HASH_VALUE_FIELD, value);
		redisService.hset(getRedisKey(key), HASH_EXPIRE_FIELD, String.valueOf(expiresTime));
	}

	private long getExpireFromRedis(String key) {
		Object expire = redisService.hget(getRedisKey(key), HASH_EXPIRE_FIELD);
		return expire == null ? 0 : Long.parseLong(expire.toString());
	}

	private void setExpire(String key, long expiresTime) {
		redisService.hset(getRedisKey(key), HASH_EXPIRE_FIELD, String.valueOf(expiresTime));
	}

	public void setRedisKeyPrefix(String redisKeyPrefix) {
		this.redisKeyPrefix = redisKeyPrefix;
	}

	public void setMaId(String maId) {
		this.maId = maId;
	}

	@Override
	public String getAccessToken() {
		return getValueFromRedis(ACCESS_TOKEN);
	}

	@Override
	public Lock getAccessTokenLock() {
		if (accessTokenLock == null) {
			synchronized (this) {
				if (accessTokenLock == null) {
					accessTokenLock = new DistributedLock(getRedisKey("accessTokenLock"));
				}
			}
		}
		return accessTokenLock;
	}

	@Override
	public boolean isAccessTokenExpired() {
		return isExpired(getExpireFromRedis(ACCESS_TOKEN));
	}

	@Override
	public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
		setValueToRedis(ACCESS_TOKEN, expiresAheadInMillis(expiresInSeconds), accessToken);
	}

	@Override
	public String getJsapiTicket() {
		return getValueFromRedis(JSAPI_TICKET);
	}

	@Override
	public Lock getJsapiTicketLock() {
		if (jsapiTicketLock == null) {
			synchronized (this) {
				if (jsapiTicketLock == null) {
					jsapiTicketLock = new DistributedLock(getRedisKey("jsapiTicketLock"));
				}
			}
		}
		return jsapiTicketLock;
	}

	@Override
	public boolean isJsapiTicketExpired() {
		return isExpired(getExpireFromRedis(JSAPI_TICKET));
	}

	@Override
	public void expireJsapiTicket() {
		setExpire(JSAPI_TICKET, 0);
	}

	@Override
	public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
		setValueToRedis(JSAPI_TICKET, expiresAheadInMillis(expiresInSeconds), jsapiTicket);
	}


	@Override
	public String getCardApiTicket() {
		return getValueFromRedis(CARD_API_TICKET);
	}

	@Override
	public Lock getCardApiTicketLock() {
		if (cardApiTicketLock == null) {
			synchronized (this) {
				if (cardApiTicketLock == null) {
					cardApiTicketLock = new DistributedLock(getRedisKey("cardApiTicketLock"));
				}
			}
		}
		return cardApiTicketLock;
	}

	@Override
	public boolean isCardApiTicketExpired() {
		return isExpired(getExpireFromRedis(CARD_API_TICKET));
	}

	@Override
	public void expireCardApiTicket() {
		setExpire(CARD_API_TICKET, 0);
	}

	@Override
	public void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
		setValueToRedis(CARD_API_TICKET, expiresAheadInMillis(expiresInSeconds), cardApiTicket);
	}

	@Override
	public void expireAccessToken() {
		setExpiresTime(0);
	}

	@Override
	public long getExpiresTime() {
		return getExpireFromRedis(ACCESS_TOKEN);
	}

	@Override
	public void setExpiresTime(long expiresTime) {
		setExpire(ACCESS_TOKEN, expiresTime);
	}

	/**
	 * 基于redis的简单分布式锁.
	 */
	private class DistributedLock implements Lock {

		private String redisKey;

		private DistributedLock(String key) {
			this.redisKey = getRedisKey(key);
		}

		@Override
		public void lock() {
			try {
				if (!redisLockService.acquire(redisKey)) {
					throw new RuntimeException("acquire timeouted");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			if (!redisLockService.acquire(redisKey)) {
				throw new RuntimeException("acquire timeouted");
			}
		}

		@Override
		public boolean tryLock() {
			try {
				return redisLockService.acquire(redisKey);
			} catch (InterruptedException e) {
				throw new RuntimeException("lock failed", e);
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			try {
				return redisLockService.acquire(redisKey);
			} catch (InterruptedException e) {
				throw new RuntimeException("lock failed", e);
			}
		}

		@Override
		public void unlock() {
			redisLockService.release(redisKey);
		}

		@Override
		public Condition newCondition() {
			throw new RuntimeException("unsupported method");
		}

	}
}
