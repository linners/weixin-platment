package com.lin.bulter.wxplatement.common.utils.redis;

/**
 * redis使用时，传递的字符串对象
 *
 * @author wangchenglin13
 * @date 2019-09-05 14:07
 */
public class RedisString {

    /**
     * 要存入redis中的数据对象
     */
    private String data;

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
    public static RedisString build(String data, Long expire){
        RedisString redisObj = new RedisString();
        redisObj.setData(data);
        redisObj.setExpire(expire);
        return redisObj;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
