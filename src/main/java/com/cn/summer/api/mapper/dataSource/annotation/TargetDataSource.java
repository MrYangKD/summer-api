package com.cn.summer.api.mapper.dataSource.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解,AOP实现分片.
 * 只能注解在实现类的方法上面
 * @author YangYK
 * @create: 2019-11-17 14:54
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    /**
     * 指定数据源.
     * @return
     */
    String value() default "";
}
