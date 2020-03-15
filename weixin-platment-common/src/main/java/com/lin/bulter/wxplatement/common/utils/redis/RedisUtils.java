package com.lin.bulter.wxplatement.common.utils.redis;

import com.alibaba.fastjson.JSON;
import com.lin.bulter.wxplatement.common.exception.AppBusinessErrorCode;
import com.lin.bulter.wxplatement.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装redis操作工具类
 *
 * @author wangchenglin13
 * @date 2019-08-20 18:28
 */
public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * json对象
     */
    public static final Integer JSON_OBJECT = 1;
    /**
     * json数组
     */
    public static final Integer JSON_ARRAY = 2;

    @FunctionalInterface
    public interface CustomStringFunction {
        RedisString doTask();
    }

    @FunctionalInterface
    public interface CustomObjFunction {
        RedisObj doTask();
    }

    @FunctionalInterface
    public interface CustomArrayFunction {
        RedisArray doTask() throws Throwable;
    }

    /**
     * 判断json字符串类型
     *
     * @param jsonStr
     * @return 1: 对象  2：数组
     */
    private static Integer jsonType(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            if (jsonStr.startsWith("{")) {
                return 1;
            } else if (jsonStr.startsWith("[")) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 从json字符串转为Bean对象
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T parseObjectFromJsonStr(String jsonStr, Class<T> cls) {
        Integer jsonType = jsonType(jsonStr);
        try {
            if (jsonType.intValue() == JSON_OBJECT) {
                return JSON.parseObject(jsonStr, cls);
            } else {
                logger.info("RedisUtils getObjectFromRedis error, redisStr is {}, is not json object", jsonStr);
                throw new Exception();
            }
        } catch (Exception e) {
            throw new BusinessException(AppBusinessErrorCode.REDIS_DATA_PARSE_ERROR);
        }
    }

    /**
     * 从json字符串转为Bean对象
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> parseArrayFromJsonStr(String jsonStr, Class<T> cls) {
        Integer jsonType = jsonType(jsonStr);
        try {
            if (jsonType.intValue() == JSON_ARRAY) {
                return JSON.parseArray(jsonStr, cls);
            } else if (jsonType.intValue() == JSON_OBJECT) {
                T t = JSON.parseObject(jsonStr, cls);
                List<T> result = new ArrayList<>();
                result.add(t);
                return result;
            } else {
                logger.info("RedisUtils getObjectFromRedis error, redisStr is {}, is not json object", jsonStr);
                throw new Exception();
            }
        } catch (Exception e) {
            throw new BusinessException(AppBusinessErrorCode.REDIS_DATA_PARSE_ERROR);
        }
    }

    /**
     * 从redis中取得key对应的对象。如果没有，则会从数据库中查询，然后放入redis中
     *
     * @param jedis
     * @param redisKey
     * @param function        回调函数
     * @return
     */
    public static String getStringFromRedis(Jedis jedis, String redisKey, CustomStringFunction function) {
        if (jedis.ttl(redisKey) == -1) {
            jedis.del(redisKey);
        }
        // 若redis中存在，则返回
        if (jedis.get(redisKey) != null) {
            return jedis.get(redisKey);
        }
        // 不存在，则回调function，将得到的值放入redis中
        else {
            RedisString redisString = function.doTask();
            if (redisString != null) {
                String data = redisString.getData();
                if (data != null) {
                    jedis.psetex(redisKey, redisString.getExpire(), data);
                    return data;
                }
            }
        }
        return null;
    }

    /**
     * 从redis中取得key对应的对象。如果没有，则会从数据库中查询，然后放入redis中
     *
     * @param jedis
     * @param redisKey
     * @param function        回调函数
     * @return
     */
    public static <T> T getObjectFromRedis(Jedis jedis, String redisKey, CustomObjFunction function, Class<T> cls) {
        if (jedis.ttl(redisKey) == -1) {
            jedis.del(redisKey);
        }
        // 若redis中存在，则返回
        if (jedis.get(redisKey) != null) {
            String redisStr = jedis.get(redisKey);
            // 转为对象
            return parseObjectFromJsonStr(redisStr, cls);
        }
        // 不存在，则回调function，将得到的值放入redis中
        else {
            RedisObj redisObj = function.doTask();
            if (redisObj != null) {
                Object data = redisObj.getData();
                if (data != null) {
                    String redisData = null;
                    if (data instanceof String) {
                        redisData = (String) data;
                    } else {
                        redisData = JSON.toJSONString(data);
                    }
                    jedis.psetex(redisKey, redisObj.getExpire(),redisData);
                    return (T) data;
                }
            }
        }
        return null;
    }

    /**
     * 从redis中取得key对应的对象。如果没有，则会从数据库中查询，然后放入redis中
     *
     * @param jedis
     * @param redisKey
     * @param function        回调函数
     * @return
     */
    public static <T> List<T> getArrayFromRedis(Jedis jedis, String redisKey, CustomArrayFunction function, Class<T> cls) {
        if (jedis.ttl(redisKey) == -1) {
            jedis.del(redisKey);
        }
        // 若redis中存在，则返回
        if (jedis.get(redisKey) != null) {
            String redisStr = jedis.get(redisKey);
            return parseArrayFromJsonStr(redisStr, cls);
        }
        // 不存在，则回调function，将得到的值放入redis中
        else {
            RedisArray redisArray = null;
            try {
                redisArray = function.doTask();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (redisArray != null) {
                List data = redisArray.getData();
                if (data != null) {
                    String redisData = JSON.toJSONString(data);
                    jedis.psetex(redisKey, redisArray.getExpire(), redisData);
                    return (List<T>) data;
                }
            }
        }
        return null;
    }
}
