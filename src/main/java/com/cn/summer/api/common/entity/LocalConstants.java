package com.cn.summer.api.common.entity;

import org.springframework.beans.factory.annotation.Value;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
public class LocalConstants {

    @Value("${cache.max.expire.seconds}")
    private static String seconds;

    @Value("${cache.max.size}")
    private static String size;

    private LocalConstants(){}

    /**缓存设置*/
    public static class CacheConfig {
        /**缓存过期时间, 默认30分钟    1800秒*/
        public final static long CACHE_MAX_EXPIRE_SECONDS = seconds != null ? Long.valueOf(seconds) : 1800;

        /**缓存最大条数 ,默认100条*/
        public final static long CACHE_MAX_SIZE = size != null ? Long.valueOf(size) : 100;
    }

    public static final class Kind{
        /** 学生类 */
        public static final String STUDENT = "STUDENT";
        /** 教师类 */
        public static final String TEACHER = "TEACHER";
    }
}
