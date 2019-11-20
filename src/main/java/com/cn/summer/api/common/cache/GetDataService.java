package com.cn.summer.api.common.cache;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.common.redis.RedisClient;
import com.cn.summer.api.common.util.SpringUtils;
import jdk.internal.dynalink.beans.StaticClass;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

/**
 * .
 *
 * @author YangYK
 * @create: 2019-11-18 16:00
 * @since 1.0
 */
public class GetDataService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(GetDataService.class);

    public static String getKey(String key) {
        RedisClient redisClient = SpringUtils.getBean(RedisClient.class);
        ShardedJedis jedis = redisClient.getRedis("checkSign");
        String publicKey = null;
        try {
            publicKey = jedis.get(key);
        } catch (Exception e) {
            LOGGER.error("查询私钥异常: " + e.getMessage(), e);
        }
        return publicKey;
    }


}
