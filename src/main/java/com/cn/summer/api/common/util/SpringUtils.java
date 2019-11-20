package com.cn.summer.api.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 实现ApplicationContextAware接口，并加入Component注解，让spring扫描到该bean.
 * 该类用于在普通Java类中注入bean,普通Java类中用@Autowired是无法注入bean的
 * @author YangYK
 * @create: 2019-11-16 14:22
 * @since 1.0
 */
@Component
public class SpringUtils implements ApplicationContextAware{
    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == SpringUtils.APPLICATION_CONTEXT){
            SpringUtils.APPLICATION_CONTEXT = applicationContext;
        }

    }


    /**
     * 获取applicationContext.
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext(){
        return APPLICATION_CONTEXT;
    }

    /**
     * 通过class获取bean.
     * @param clazz clazz
     * @param <T> T
     * @return T
     */
    public static <T> T getBean(final Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name获取 Bean.
     * @param name name
     * @return Object
     */
    public static Object getBean(final String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean.
     * @param name name
     * @param clazz clazz
     * @param <T>  T
     * @return T
     */
    public static <T> T getBean(final String name, final Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }
}
