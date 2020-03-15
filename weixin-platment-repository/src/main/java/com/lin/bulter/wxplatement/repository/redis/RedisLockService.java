package com.lin.bulter.wxplatement.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/14 15:51
 */
@Component
public class RedisLockService {

	private int expireMsecs = 60000;
	private int timeoutMsecs = 10000;
	private boolean locked = false;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 加锁
	 * @param lockKey
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized boolean acquire(String lockKey) throws InterruptedException {
		int timeout = this.timeoutMsecs;

		while(timeout >= 0) {
			long expires = System.currentTimeMillis() + (long)this.expireMsecs + 1L;
			String expiresStr = String.valueOf(expires);
			if (redisTemplate.opsForValue().setIfAbsent(lockKey, expiresStr)) {
				this.locked = true;
				return true;
			}

			String currentValueStr = (String) redisTemplate.opsForValue().get(lockKey);
			if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
				String oldValueStr = (String) redisTemplate.opsForValue().getAndSet(lockKey, expiresStr);
				if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					this.locked = true;
					return true;
				}
			}

			timeout -= 100;
			Thread.sleep(100L);
		}

		return false;
	}

	/**
	 * 释放锁
	 */
	public synchronized void release(String lockKey) {
		if (this.locked) {
			redisTemplate.delete(lockKey);
			this.locked = false;
		}

	}
}
