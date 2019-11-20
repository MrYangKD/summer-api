package com.cn.summer.api.common.redis;

import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Sharded;

import java.util.List;
import java.util.Map;

import static redis.clients.util.Hashing.MURMUR_HASH;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
@Configuration
@EnableCaching
public class RedisPluginLoader {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RedisPluginLoader.class);

    private static final Boolean DEFAULT_TEST_ON_BORROW = Boolean.FALSE;

    public static Map<String, RedisClientImpl> globalsRedis = Maps.newHashMap();

    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public JedisConnectionFactory initPlugin() throws Exception{
        loadRedis();
        return new JedisConnectionFactory();

    }

    private void loadRedis(){
        if (redisConfig.getRedisType().size()>0){
            for (int i = 0; i < redisConfig.getHostNames().size(); i++){
                JedisPoolConfig jedisPoolConfig = getJedisPoolConfig(redisConfig.getPoolMaxIdle().get(i), redisConfig.getPoolMinIdle().get(i));
                ShardedJedisPool shardedJedisPool = createJedisPool(redisConfig.getHostNames().get(i), redisConfig.getTimeout().get(i), jedisPoolConfig);
                globalsRedis.put(redisConfig.getRedisType().get(i), new RedisClientImpl(shardedJedisPool));
            }
        }
    }

    private JedisPoolConfig getJedisPoolConfig(String maxTotal, String maxIdle){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.valueOf(maxIdle));
        poolConfig.setMaxTotal(Integer.valueOf(maxTotal));
        poolConfig.setTestOnBorrow(DEFAULT_TEST_ON_BORROW);
        return poolConfig;
    }

    private ShardedJedisPool createJedisPool(String clusterNodes, String timeout, JedisPoolConfig jedisPoolConfig){
        try {
            String[] hostAndports = clusterNodes.split(";");
            List shards = Lists.newArrayList();
            for (int i = 0; i < hostAndports.length; i++){
                String [] hostPort = hostAndports[i].split(":");
                shards.add(new JedisShardInfo(hostPort[0], Integer.valueOf(hostPort[1]), Integer.valueOf(timeout)));
            }
            return new ShardedJedisPool(jedisPoolConfig, shards, MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
        } catch (Exception e){
            LOGGER.error("初始化redis异常" + e.getMessage(), e);
        }
        return null;
    }
}
