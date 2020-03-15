package com.lin.bulter.wxplatement.common.utils.redis;

import java.util.List;

/**
 * redis使用时，传递的list对象
 *
 * @author wangchenglin13
 * @date 2019-09-05 14:07
 */
public class RedisArray {

    /**
     * 要存入redis中的数据对象
     */
    private List data;

    /**
     * 过期时间，单位：秒
     */
    private Long expire;

    /**
     * 构建实例对象
     * @param data  要缓存的数据
     * @param expire 过期时间，单位：秒
     * @return 对象实例
     */
    public static RedisArray build(List data, Long expire){
        RedisArray redisObj = new RedisArray();
        redisObj.setData(data);
        redisObj.setExpire(expire);
        return redisObj;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
