package com.cn.summer.api.common.cache;

import ch.qos.logback.classic.Logger;
import com.cn.summer.api.common.entity.LocalConstants;
import com.cn.summer.api.common.redis.RedisClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.ShardedJedis;

import java.util.concurrent.TimeUnit;

/**
 * 公钥缓存维护.
 *
 * @author YangYK
 * @since 1.0
 */
public final class CacheInfo {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CacheInfo.class);


    private static LoadingCache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(LocalConstants.CacheConfig.CACHE_MAX_EXPIRE_SECONDS, TimeUnit.SECONDS)
            .maximumSize(LocalConstants.CacheConfig.CACHE_MAX_SIZE).build(new CacheLoader<String, String>() {

                @Override
                public String load(String key) throws Exception {
                    return GetDataService.getKey(key);
                }
            });

    private CacheInfo() {

    }

    public static String getPublicKey(final String key) {
        String publicKey = null;
        try {
            publicKey = CACHE.get(key);
        } catch (Exception e) {
            LOGGER.error("查询缓存异常: " + e.getMessage(), e);
        } finally {
            return publicKey;
        }
    }
}
