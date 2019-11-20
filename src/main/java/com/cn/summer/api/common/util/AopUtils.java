package com.cn.summer.api.common.util;

import ch.qos.logback.classic.Logger;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * AOP获取参数工具类.
 *
 * @author YangYK
 * @since 1.0
 */
public final class AopUtils {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AopUtils.class);
    private AopUtils(){}

    /**
     * 获取所有请求参数.
     * @param joinPoint 切点
     * @return Map
     */
    public static Map<String,Object> getParams(final JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        Map<String, Object> params = Maps.newHashMap();

        for (int i = 0; i < parameterNames.length; i++){
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }

    /**
     * 根据参数名称获取相应的值.
     * @param joinPoint 切点
     * @param name 参数名称
     * @return Object
     */
    public static Object getParamValueByName(final JoinPoint joinPoint, final String name){
        Map<String, Object> params = getParams(joinPoint);
        return params.get(name);
    }

}
