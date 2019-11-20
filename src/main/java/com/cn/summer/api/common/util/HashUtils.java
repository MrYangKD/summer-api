package com.cn.summer.api.common.util;

/**
 * .
 * Hash运算,参考MQ写入队列hash算法.
 * @author YangYK
 * @create: 2019-11-17 15:12
 * @since 1.0
 */
public final class HashUtils {
    private static final int PLACES = 16;
    private static final int ZERO = 0;
    private static final int ONE = 1;

    private HashUtils(){}

    public static int hashSplit(final Object key, final int size){
        int hash = Math.abs(hash(key));
        if ((size & size - 1) == 0){
            //2的指数倍数，使用hashmap的算法
            return (size - 1) & hash;
        }else {
            //不然直接模除.
            return hash % size;
        }

    }

    public static int hash(final Object key){
        int h;
        return (key == null) ? ZERO : (h= key.hashCode()) ^ (h >>> PLACES);
    }
}
